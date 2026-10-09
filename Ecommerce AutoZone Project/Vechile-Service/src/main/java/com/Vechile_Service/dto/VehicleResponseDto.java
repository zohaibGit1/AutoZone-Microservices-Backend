package com.Vechile_Service.dto;

import com.Vechile_Service.constant.VehicleType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleResponseDto {
    private UUID vehicleId;
    private String vehicleNumber;
    private String vehicleName;
    private String vehicleModel;
    private VehicleType vehicleType;
    private UUID customerId;
}
