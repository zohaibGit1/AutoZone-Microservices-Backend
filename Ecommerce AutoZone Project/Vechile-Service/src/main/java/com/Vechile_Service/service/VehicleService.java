package com.Vechile_Service.service;

import com.Vechile_Service.dto.VehicleRequestDto;
import com.Vechile_Service.dto.VehicleResponseDto;

import java.util.List;
import java.util.UUID;

public interface VehicleService {
    VehicleResponseDto registerVehicle(VehicleRequestDto vehicleRequestDto);
    VehicleResponseDto getVehicleById(UUID vehicleId);
    List<VehicleResponseDto> getVehiclesByCustomerId(UUID customerId);
    void deleteVehicle(UUID vehicleId);
}
