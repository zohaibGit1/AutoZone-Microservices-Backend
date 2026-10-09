package com.Booking_Serivce.Dto;

import com.Booking_Serivce.Constant.BookingStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponseDto {

    private UUID bookingId;

    private UUID customerId;

    private UUID vehicleId;

    private UUID garageId;

    private UUID serviceId;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private BookingStatus bookingStatus;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}