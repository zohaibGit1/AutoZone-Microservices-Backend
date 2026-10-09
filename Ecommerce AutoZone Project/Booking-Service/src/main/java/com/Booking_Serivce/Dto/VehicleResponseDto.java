package com.Booking_Serivce.Dto;

import com.Booking_Serivce.Constant.VehicleType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleResponseDto {
    private UUID vehicleId;
    private String vehicleNumber;
    private String vehicleName;
    private String vehicleModel;
    private VehicleType vehicleType;
    private UUID customerId;
}
