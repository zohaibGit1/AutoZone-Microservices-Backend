package com.Booking_Serivce.Controller;

import com.Booking_Serivce.Dto.AvailabilityResponseDto;
import com.Booking_Serivce.Dto.BookingRequestDto;
import com.Booking_Serivce.Dto.BookingResponseDto;
import com.Booking_Serivce.Service.BookingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * REST Controller for managing bookings and slot availability in Booking Service.
 * Follows industry best practices for distributed microservice communication,
 * standard HTTP status codes, structured logging, and input validation.
 */
@Slf4j
@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    /**
     * Create a new booking with validation, capacity checks, and pessimistic garage locking.
     *
     * @param request Booking creation request payload
     * @return 201 Created with BookingResponseDto
     */
    @PostMapping({"", "/create"})
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingRequestDto request) {
        log.info("REST request to create booking: customerId={}, vehicleId={}, garageId={}, serviceId={}, startTime={}",
                request.getCustomerId(), request.getVehicleId(), request.getGarageId(), request.getServiceId(), request.getStartTime());
        BookingResponseDto response = bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    /**
     * Query real-time slot availability for a given garage, service type, and date.
     *
     * @param garageId  UUID string of the garage
     * @param serviceId UUID string of the service
     * @param date      Target date in ISO format (YYYY-MM-DD)
     * @return 200 OK with AvailabilityResponseDto containing remaining capacity per slot
     */
    @GetMapping("/availability")
    public ResponseEntity<AvailabilityResponseDto> getAvailability(
            @RequestParam String garageId,
            @RequestParam String serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        log.info("REST request for slot availability: garageId={}, serviceId={}, date={}", garageId, serviceId, date);
        AvailabilityResponseDto response = bookingService.getAvailability(garageId, serviceId, date);
        return ResponseEntity.ok(response);
    }

    /**
     * Retrieve booking details by booking ID.
     */
    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> getBookingById(@PathVariable String bookingId) {
        log.info("REST request to get booking: bookingId={}", bookingId);
        return ResponseEntity.ok(bookingService.getBookingById(bookingId));
    }

    /**
     * Retrieve all bookings associated with a specific customer.
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByCustomer(@PathVariable String customerId) {
        log.info("REST request to get bookings for customer: customerId={}", customerId);
        return ResponseEntity.ok(bookingService.getBookingsByCustomer(customerId));
    }

    /**
     * Retrieve all bookings associated with a specific vehicle.
     */
    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByVehicle(@PathVariable String vehicleId) {
        log.info("REST request to get bookings for vehicle: vehicleId={}", vehicleId);
        return ResponseEntity.ok(bookingService.getBookingsByVehicle(vehicleId));
    }

    /**
     * Retrieve all bookings for a specific garage.
     */
    @GetMapping("/garage/{garageId}")
    public ResponseEntity<List<BookingResponseDto>> getBookingsByGarage(@PathVariable String garageId) {
        log.info("REST request to get bookings for garage: garageId={}", garageId);
        return ResponseEntity.ok(bookingService.getBookingsByGarage(garageId));
    }

    /**
     * Cancel a booking (releases allocated garage capacity).
     */
    @PatchMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponseDto> cancelBooking(@PathVariable String bookingId) {
        log.info("REST request to cancel booking: bookingId={}", bookingId);
        return ResponseEntity.ok(bookingService.cancelBooking(bookingId));
    }

    /**
     * Confirm a pending booking.
     */
    @PatchMapping("/{bookingId}/confirm")
    public ResponseEntity<BookingResponseDto> confirmBooking(@PathVariable String bookingId) {
        log.info("REST request to confirm booking: bookingId={}", bookingId);
        return ResponseEntity.ok(bookingService.confirmBooking(bookingId));
    }

    /**
     * Mark vehicle arrival at the garage (CHECKED_IN).
     */
    @PatchMapping("/{bookingId}/check-in")
    public ResponseEntity<BookingResponseDto> checkInBooking(@PathVariable String bookingId) {
        log.info("REST request to check-in booking: bookingId={}", bookingId);
        return ResponseEntity.ok(bookingService.checkInBooking(bookingId));
    }

    /**
     * Mark service started on the vehicle (IN_PROGRESS).
     */
    @PatchMapping("/{bookingId}/start")
    public ResponseEntity<BookingResponseDto> startBooking(@PathVariable String bookingId) {
        log.info("REST request to start booking: bookingId={}", bookingId);
        return ResponseEntity.ok(bookingService.startBooking(bookingId));
    }

    /**
     * Mark service completed (COMPLETED).
     */
    @PatchMapping("/{bookingId}/complete")
    public ResponseEntity<BookingResponseDto> completeBooking(@PathVariable String bookingId) {
        log.info("REST request to complete booking: bookingId={}", bookingId);
        return ResponseEntity.ok(bookingService.completeBooking(bookingId));
    }

    /**
     * Mark booking as customer NO_SHOW (frees up garage capacity).
     */
    @PatchMapping("/{bookingId}/no-show")
    public ResponseEntity<BookingResponseDto> markAsNoShow(@PathVariable String bookingId) {
        log.info("REST request to mark booking as no-show: bookingId={}", bookingId);
        return ResponseEntity.ok(bookingService.markAsNoShow(bookingId));
    }
}
