package com.Payment_Service.dto;

import com.Payment_Service.constant.BookingStatus;
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
