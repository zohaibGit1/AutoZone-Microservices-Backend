package com.Vechile_Service.dto;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class VehicleVisitResponse {
    private UUID visitId;
    private LocalDate visitDate;
    private int currentKm;
    private LocalDateTime checkInTime;
    private UUID vehicleId;
    private UUID bookingId;
}
