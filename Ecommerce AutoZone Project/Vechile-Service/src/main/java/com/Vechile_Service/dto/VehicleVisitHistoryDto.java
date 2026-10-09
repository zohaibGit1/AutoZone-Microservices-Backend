package com.Vechile_Service.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleVisitHistoryDto {

    private UUID visitId;
    private LocalDate visitDate;
    private Integer currentKm;
    private UUID vehicleId;
    private String vehicleNumber;
    private List<ComplaintResponseDto> complaints;
}
