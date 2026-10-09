package com.Vechile_Service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VehicleVisitRequestDto {

    @NotNull(message = "Current KM is required")
    @Min(value = 0, message = "Current KM cannot be negative")
    private Integer currentKm;

    @NotNull(message = "Vehicle ID is required")
    private UUID vehicleId;

    // Optional — set when this visit originates from a confirmed Booking Service appointment.
    // Left null for walk-in visits that don't go through Booking Service.
    private UUID bookingId;
}
