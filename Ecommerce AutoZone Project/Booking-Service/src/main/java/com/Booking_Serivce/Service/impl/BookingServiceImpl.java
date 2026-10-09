package com.Booking_Serivce.Service.impl;

import com.Booking_Serivce.Client.CustomerClient;
import com.Booking_Serivce.Client.VehicleClient;
import com.Booking_Serivce.Constant.BookingStatus;
import com.Booking_Serivce.Constant.EntityStatus;
import com.Booking_Serivce.Dto.*;
import com.Booking_Serivce.Entity.Booking;
import com.Booking_Serivce.Entity.Garage;
import com.Booking_Serivce.Entity.ServiceType;
import com.Booking_Serivce.Exception.ExceptionClass.InvalidBookingException;
import com.Booking_Serivce.Exception.ExceptionClass.ResourceNotFoundException;
import com.Booking_Serivce.Mapper.BookingMapper;
import com.Booking_Serivce.Repository.BookingRepository;
import com.Booking_Serivce.Repository.GarageRepository;
import com.Booking_Serivce.Repository.ServiceRepository;
import com.Booking_Serivce.Service.BookingService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final VehicleClient vehicleClient;
    private final CustomerClient customerClient;
    private final ServiceRepository serviceTypeRepository;
    private final GarageRepository garageRepository;

    private static final Set<BookingStatus> CAPACITY_CONSUMING_STATUSES = Set.of(
            BookingStatus.PENDING,
            BookingStatus.CONFIRMED,
            BookingStatus.CHECKED_IN,
            BookingStatus.IN_PROGRESS
    );

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            BookingMapper bookingMapper,
            VehicleClient vehicleClient,
            CustomerClient customerClient,
            ServiceRepository serviceTypeRepository,
            GarageRepository garageRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.vehicleClient = vehicleClient;
        this.customerClient = customerClient;
        this.serviceTypeRepository = serviceTypeRepository;
        this.garageRepository = garageRepository;
    }

    @Override
    @Transactional
    public BookingResponseDto createBooking(BookingRequestDto request) {
        // 1. Fetch & Verify Customer Existence via CustomerVehicleService Feign Client
        CustomerResponseDto customerResponseDto;
        try {
            log.info("Fetching Customer details from CustomerVehicleService for ID: {}", request.getCustomerId());
            customerResponseDto = customerClient.getCustomerById(request.getCustomerId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Customer not found with ID: " + request.getCustomerId());
        }

        // 2. Fetch & Verify Vehicle Existence via CustomerVehicleService Feign Client
        VehicleResponseDto vehicleResponseDto;
        try {
            log.info("Fetching Vehicle details from CustomerVehicleService for ID: {}", request.getVehicleId());
            vehicleResponseDto = vehicleClient.getVehicleById(request.getVehicleId());
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Vehicle not found with ID: " + request.getVehicleId());
        }

        // 3. Verify vehicle belongs to customer
        if (vehicleResponseDto.getCustomerId() == null || !vehicleResponseDto.getCustomerId().equals(request.getCustomerId())) {
            log.warn("Vehicle ID {} does not belong to Customer ID {}", request.getVehicleId(), request.getCustomerId());
            throw new InvalidBookingException("Vehicle with ID " + request.getVehicleId() + " does not belong to customer with ID " + request.getCustomerId());
        }

        // 4. Validate booking start time is not in the past
        if (request.getStartTime().isBefore(LocalDateTime.now())) {
            throw new InvalidBookingException("Booking start time cannot be in the past");
        }

        // 5. Lock Garage row during capacity check to prevent race conditions (Pessimistic Locking)
        Garage garage = garageRepository.findByIdForUpdate(request.getGarageId())
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with ID: " + request.getGarageId()));

        if (garage.getStatus() != EntityStatus.ACTIVE) {
            throw new InvalidBookingException("Garage is currently not active");
        }

        // 6. Verify Weekly Off by parsing into a Set of DayOfWeek
        if (garage.getWeeklyOff() != null && !garage.getWeeklyOff().isBlank()) {
            DayOfWeek bookingDay = request.getStartTime().getDayOfWeek();
            Set<DayOfWeek> weeklyOffDays = Arrays.stream(garage.getWeeklyOff().split("[,;|]+"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toUpperCase)
                    .map(s -> {
                        try {
                            return DayOfWeek.valueOf(s);
                        } catch (IllegalArgumentException e) {
                            log.warn("Invalid day of week '{}' found in garage weeklyOff", s);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (weeklyOffDays.contains(bookingDay)) {
                throw new InvalidBookingException("Garage is closed on " + bookingDay + "s (Weekly Off)");
            }
        }

        // 7. Verify Service Type Existence & Status, then calculate endTime
        ServiceType serviceType = serviceTypeRepository.findById(request.getServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service Type not found with ID: " + request.getServiceId()));

        if (serviceType.getStatus() != EntityStatus.ACTIVE) {
            throw new InvalidBookingException("Service Type is currently not active");
        }

        LocalDateTime endTime = request.getStartTime().plusMinutes(serviceType.getEstimatedDurationMinutes());

        // 8. Verify Garage Operating Hours
        LocalTime openingTime = garage.getOpeningTime();
        LocalTime closingTime = garage.getClosingTime();
        LocalTime bookingStartTime = request.getStartTime().toLocalTime();
        LocalTime bookingEndTime = endTime.toLocalTime();

        if (openingTime != null && bookingStartTime.isBefore(openingTime)) {
            throw new InvalidBookingException(String.format(
                    "Booking start time (%s) cannot be before garage opening time (%s)",
                    bookingStartTime, openingTime
            ));
        }

        if (closingTime != null && (!endTime.toLocalDate().isEqual(request.getStartTime().toLocalDate()) || bookingEndTime.isAfter(closingTime))) {
            throw new InvalidBookingException(String.format(
                    "Booking end time (%s) exceeds garage closing time (%s)",
                    bookingEndTime, closingTime
            ));
        }

        // 9. Capacity check
        // Check if the same vehicle already has an active overlapping booking
        long overlappingVehicleBookings = bookingRepository.countOverlappingBookingsForVehicle(
                request.getVehicleId(),
                request.getStartTime(),
                endTime,
                CAPACITY_CONSUMING_STATUSES
        );
        if (overlappingVehicleBookings > 0) {
            throw new InvalidBookingException("Vehicle already has an active booking during the selected time slot");
        }

        // Check if garage has reached full capacity
        int capacity = garage.getCapacity() != null ? garage.getCapacity() : 0;
        long overlappingGarageBookings = bookingRepository.countOverlappingBookingsForGarage(
                garage.getGarageId(),
                request.getStartTime(),
                endTime,
                CAPACITY_CONSUMING_STATUSES
        );

        if (overlappingGarageBookings >= capacity) {
            log.warn("Garage {} is at full capacity ({}/{} slots occupied) for time slot {} to {}",
                    garage.getGarageId(), overlappingGarageBookings, capacity, request.getStartTime(), endTime);
            throw new InvalidBookingException("Garage has reached full capacity for the selected time slot");
        }

        // 10. Save booking
        Booking booking = Booking.builder()
                .customerId(request.getCustomerId())
                .vehicleId(request.getVehicleId())
                .garage(garage)
                .serviceType(serviceType)
                .startTime(request.getStartTime())
                .endTime(endTime)
                .bookingStatus(BookingStatus.PENDING)
                .build();

        Booking savedBooking = bookingRepository.save(booking);
        log.info("Booking successfully created with ID: {}", savedBooking.getBookingId());

        // TODO: [Kafka Integration - Producer]
        // In the future, publish BookingCreatedEvent to Kafka topic (e.g. "booking-events"):
        //   - Payment Service: initiates payment / checkout session for this booking
        //   - Notification Service: sends SMS / Email confirmation to the customer
        // Example:
        // kafkaTemplate.send("booking-events", savedBooking.getBookingId().toString(), bookingCreatedEvent);

        return bookingMapper.toResponse(savedBooking);
    }

    private UUID parseUuid(String id, String fieldName) {
        if (id == null || id.isBlank()) {
            throw new InvalidBookingException(fieldName + " must not be blank");
        }
        try {
            return UUID.fromString(id.trim());
        } catch (IllegalArgumentException e) {
            throw new InvalidBookingException("Invalid UUID format for " + fieldName + ": " + id);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public BookingResponseDto getBookingById(String bookingId) {
        UUID id = parseUuid(bookingId, "Booking ID");
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
        return bookingMapper.toResponse(booking);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingsByCustomer(String customerId) {
        UUID id = parseUuid(customerId, "Customer ID");
        try {
            log.info("Fetching Customer details from Customer Client for ID: {}", id);
            customerClient.getCustomerById(id);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Customer not found with ID: " + customerId);
        }
        return bookingRepository.findByCustomerId(id)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingsByVehicle(String vehicleId) {
        UUID id = parseUuid(vehicleId, "Vehicle ID");
        try {
            log.info("Fetching Vehicle details from Vehicle Client for ID: {}", id);
            vehicleClient.getVehicleById(id);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Vehicle not found with ID: " + id);
        }

        return bookingRepository.findByVehicleId(id)
                .stream()
                .map(bookingMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BookingResponseDto> getBookingsByGarage(String garageId) {
        UUID id = parseUuid(garageId, "Garage ID");
        garageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found with ID: " + garageId));

        List<Booking> garageAllBooking = bookingRepository.findByGarageId(id);
        return garageAllBooking.stream()
                .map(bookingMapper::toResponse)
                .collect(Collectors.toList());
    }

    // TODO: Most Complex
    @Override
    @Transactional(readOnly = true)
    public AvailabilityResponseDto getAvailability(String garageId, String serviceId, LocalDate date) {
        UUID gId = UUID.fromString(garageId);
        UUID sId = UUID.fromString(serviceId);

        Garage garage = garageRepository.findById(gId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Garage not Found with the Id " + garageId)
                );
        ServiceType serviceType = serviceTypeRepository.findById(sId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Service Type not Found with the Id " + serviceId)
                );

        if (garage.getStatus() != EntityStatus.ACTIVE) {
            throw new InvalidBookingException("Garage is currently not active");
        }

        if (serviceType.getStatus() != EntityStatus.ACTIVE) {
            throw new InvalidBookingException("Service Type is currently not active");
        }

        // 1. If date is in the past, return empty slots
        if (date.isBefore(LocalDate.now())) {
            return AvailabilityResponseDto.builder()
                    .garageId(gId)
                    .serviceId(sId)
                    .date(date)
                    .availableSlots(Collections.emptyList())
                    .build();
        }

        // 2. Check Garage Weekly Off
        if (garage.getWeeklyOff() != null && !garage.getWeeklyOff().isBlank()) {
            DayOfWeek dayOfWeek = date.getDayOfWeek();
            Set<DayOfWeek> weeklyOffDays = Arrays.stream(garage.getWeeklyOff().split("[,;|]+"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toUpperCase)
                    .map(s -> {
                        try {
                            return DayOfWeek.valueOf(s);
                        } catch (IllegalArgumentException e) {
                            log.warn("Invalid day of week '{}' found in garage weeklyOff", s);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (weeklyOffDays.contains(dayOfWeek)) {
                log.info("Garage {} is closed on {} (Weekly Off)", garageId, dayOfWeek);
                return AvailabilityResponseDto.builder()
                        .garageId(gId)
                        .serviceId(sId)
                        .date(date)
                        .availableSlots(Collections.emptyList())
                        .build();
            }
        }

        LocalTime openingTime = garage.getOpeningTime();
        LocalTime closingTime = garage.getClosingTime();
        int durationMinutes = serviceType.getEstimatedDurationMinutes() != null ? serviceType.getEstimatedDurationMinutes() : 0;
        int totalCapacity = garage.getCapacity() != null ? garage.getCapacity() : 0;

        if (openingTime == null || closingTime == null || !openingTime.isBefore(closingTime) || durationMinutes <= 0 || totalCapacity <= 0) {
            return AvailabilityResponseDto.builder()
                    .garageId(gId)
                    .serviceId(sId)
                    .date(date)
                    .availableSlots(Collections.emptyList())
                    .build();
        }

        // 3. Fetch all active bookings for this garage on this date in a single query
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.plusDays(1).atStartOfDay();

        List<Booking> dayBookings = bookingRepository.findActiveBookingsForGarageOnDay(
                gId,
                dayStart,
                dayEnd,
                CAPACITY_CONSUMING_STATUSES
        );

        // 4. Generate candidate slots every 30 mins (or durationMinutes if < 30)
        int stepMinutes = (durationMinutes < 30) ? durationMinutes : 30;

        LocalTime currentSlotTime = openingTime;
        LocalDateTime now = LocalDateTime.now();
        List<AvailableSlotDto> availableSlots = new ArrayList<>();

        while (true) {
            LocalDateTime slotStartTime = LocalDateTime.of(date, currentSlotTime);
            LocalDateTime slotEndTime = slotStartTime.plusMinutes(durationMinutes);

            // If slot extends beyond closing time or rolls into next day, stop generating slots
            if (!slotEndTime.toLocalDate().isEqual(date) || slotEndTime.toLocalTime().isAfter(closingTime)) {
                break;
            }

            // For today's date, skip slots whose start time has already passed
            if (!date.isEqual(LocalDate.now()) || !slotStartTime.isBefore(now)) {
                // Count active bookings overlapping with this slot: [slotStartTime, slotEndTime]
                long overlappingBookings = dayBookings.stream()
                        .filter(b -> b.getStartTime().isBefore(slotEndTime) && b.getEndTime().isAfter(slotStartTime))
                        .count();

                int availableCapacity = (int) (totalCapacity - overlappingBookings);

                if (availableCapacity > 0) {
                    availableSlots.add(AvailableSlotDto.builder()
                            .startTime(slotStartTime)
                            .endTime(slotEndTime)
                            .availableCapacity(availableCapacity)
                            .build());
                }
            }

            currentSlotTime = currentSlotTime.plusMinutes(stepMinutes);
            if (!currentSlotTime.isBefore(closingTime)) {
                break;
            }
        }

        return AvailabilityResponseDto.builder()
                .garageId(gId)
                .serviceId(sId)
                .date(date)
                .availableSlots(availableSlots)
                .build();
    }

    @Override
    @Transactional
    public BookingResponseDto cancelBooking(String bookingId) {
        UUID id = parseUuid(bookingId, "Booking ID");
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
        if (booking.getBookingStatus() == BookingStatus.COMPLETED ||
                booking.getBookingStatus() == BookingStatus.CANCELLED ||
                booking.getBookingStatus() == BookingStatus.NO_SHOW ||
                booking.getBookingStatus() == BookingStatus.IN_PROGRESS) {

            throw new InvalidBookingException(
                    "Booking cannot be cancelled in status: "
                            + booking.getBookingStatus());
        }

        booking.setBookingStatus(BookingStatus.CANCELLED);
        log.info("Booking ID {} cancelled successfully", bookingId);

        return bookingMapper.toResponse(
                bookingRepository.save(booking)
        );
    }

    @Override
    @Transactional
    public BookingResponseDto confirmBooking(String bookingId) {
        UUID id = parseUuid(bookingId, "Booking ID");
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getBookingStatus() != BookingStatus.PENDING) {
            throw new InvalidBookingException(
                    "Booking cannot be confirmed in status: "
                            + booking.getBookingStatus());
        }
        // TODO: here in future we have to consume the payment-event and update the booking status from KAFKA
        // TODO: Future:
        // TODO: Payment event → Kafka → this status update

        booking.setBookingStatus(BookingStatus.CONFIRMED);
        log.info("Booking ID {} confirmed successfully", bookingId);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto checkInBooking(String bookingId) {
        UUID id = parseUuid(bookingId, "Booking ID");
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidBookingException(
                    "Booking cannot be checked in in status: "
                            + booking.getBookingStatus());
        }
        booking.setBookingStatus(BookingStatus.CHECKED_IN);
        log.info("Booking ID {} checked in successfully", bookingId);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto startBooking(String bookingId) {
        UUID id = parseUuid(bookingId, "Booking ID");
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (booking.getBookingStatus() != BookingStatus.CHECKED_IN) {
            throw new InvalidBookingException(
                    "Booking cannot be started in status: "
                            + booking.getBookingStatus());
        }
        booking.setBookingStatus(BookingStatus.IN_PROGRESS);
        log.info("Booking ID {} started (IN_PROGRESS) successfully", bookingId);
        return bookingMapper.toResponse(bookingRepository.save(booking));
    }

    @Override
    @Transactional
    public BookingResponseDto completeBooking(String bookingId) {
        UUID id = parseUuid(bookingId, "Booking ID");
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
        if (booking.getBookingStatus() != BookingStatus.IN_PROGRESS) {
            throw new InvalidBookingException(
                    "Booking cannot be completed in status: "
                            + booking.getBookingStatus());
        }
        booking.setBookingStatus(BookingStatus.COMPLETED);
        log.info("Booking ID {} completed successfully", bookingId);
        return bookingMapper.toResponse(
                bookingRepository.save(booking)
        );
    }

    // Customer had a confirmed booking but did not arrive at the garage within the allowed time.
    @Override
    @Transactional
    public BookingResponseDto markAsNoShow(String bookingId) {
        UUID id = parseUuid(bookingId, "Booking ID");
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
        if (booking.getBookingStatus() != BookingStatus.CONFIRMED) {
            throw new InvalidBookingException(
                    "Booking cannot be marked as NO_SHOW in status: "
                            + booking.getBookingStatus());
        }
        booking.setBookingStatus(BookingStatus.NO_SHOW);
        log.info("Booking ID {} marked as NO_SHOW successfully", bookingId);
        return bookingMapper.toResponse(
                bookingRepository.save(booking)
        );

        // TODO: [Future Automation]
        // Automatically mark CONFIRMED bookings as NO_SHOW after the configured
        // grace period if the customer has not checked in.
        // This should be triggered by a scheduled job/event-driven workflow.
        // Example: Appointment at 10:00 → grace period expires → still CONFIRMED → NO_SHOW.
    }

    // TODO: [Kafka Integration - Consumer / Payment Event Listener]
    // In the future, listen for payment result events to asynchronously update booking status:
    //   - If SUCCESS: update status to CONFIRMED
    //   - If FAILED or TIMED OUT: update status to PAYMENT_FAILED (or CANCELLED), freeing up garage capacity
    // Example:
    // @KafkaListener(topics = "${kafka.topics.payment-result:payment-events}", groupId = "booking-service-group")
    // public void handlePaymentResult(PaymentEvent paymentEvent) {
    //     Booking booking = bookingRepository.findById(paymentEvent.getBookingId())
    //             .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + paymentEvent.getBookingId()));
    //     if ("SUCCESS".equalsIgnoreCase(paymentEvent.getStatus())) {
    //         booking.setBookingStatus(BookingStatus.CONFIRMED);
    //     } else {
    //         booking.setBookingStatus(BookingStatus.PAYMENT_FAILED);
    //     }
    //     bookingRepository.save(booking);
    // }
}
