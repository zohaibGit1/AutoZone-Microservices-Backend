package com.Booking_Serivce.Service;

import com.Booking_Serivce.Dto.AvailabilityResponseDto;
import com.Booking_Serivce.Dto.BookingRequestDto;
import com.Booking_Serivce.Dto.BookingResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface BookingService {

    BookingResponseDto createBooking(BookingRequestDto request);

    BookingResponseDto getBookingById(String bookingId);

    List<BookingResponseDto> getBookingsByCustomer(String customerId);

    List<BookingResponseDto> getBookingsByVehicle(String vehicleId);

    List<BookingResponseDto> getBookingsByGarage(String garageId);

    AvailabilityResponseDto getAvailability(
            String garageId,
            String serviceId,
            LocalDate date
    );

    BookingResponseDto cancelBooking(String bookingId);

    BookingResponseDto confirmBooking(String bookingId);

    BookingResponseDto checkInBooking(String bookingId);

    BookingResponseDto startBooking(String bookingId);

    BookingResponseDto completeBooking(String bookingId);

    BookingResponseDto markAsNoShow(String bookingId);
}
