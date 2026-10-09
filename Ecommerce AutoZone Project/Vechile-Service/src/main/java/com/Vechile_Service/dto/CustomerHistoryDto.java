package com.Vechile_Service.dto;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerHistoryDto {

    private UUID customerId;

    private String customerName;

    private String customerEmail;

    private String customerPhone;

    private List<VehicleResponseDto> vehicles;

    private List<VehicleVisitHistoryDto> vehicleVisits;
}
