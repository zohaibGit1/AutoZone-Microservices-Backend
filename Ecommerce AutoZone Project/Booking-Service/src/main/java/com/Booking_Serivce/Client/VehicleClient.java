package com.Booking_Serivce.Client;

import com.Booking_Serivce.Dto.VehicleResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name = "${services.customer-vehicle-service.name}",
        contextId = "vehicleClient"
)
public interface VehicleClient {
    @GetMapping("/api/v1/vehicle/{vehicleId}")
    VehicleResponseDto getVehicleById(@PathVariable UUID vehicleId);
}
