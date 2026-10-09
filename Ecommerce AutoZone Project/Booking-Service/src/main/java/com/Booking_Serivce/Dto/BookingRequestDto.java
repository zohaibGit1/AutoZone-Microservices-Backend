package com.Booking_Serivce.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequestDto {

    @NotNull(message = "Customer ID is required")
    private UUID customerId;

    @NotNull(message = "Vehicle ID is required")
    private UUID vehicleId;

    @NotNull(message = "Garage ID is required")
    private UUID garageId;

    @NotNull(message = "Service ID is required")
    private UUID serviceId;

    @NotNull(message = "Start time is required")
    private LocalDateTime startTime;
}
