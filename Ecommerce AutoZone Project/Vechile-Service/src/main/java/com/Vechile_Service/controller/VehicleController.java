package com.Vechile_Service.controller;

import com.Vechile_Service.dto.VehicleRequestDto;
import com.Vechile_Service.dto.VehicleResponseDto;
import com.Vechile_Service.service.VehicleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicle")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    @PostMapping({"", "/register-vehicle"})
    public ResponseEntity<VehicleResponseDto> registerVehicle(@Valid @RequestBody VehicleRequestDto vehicleRequestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehicleService.registerVehicle(vehicleRequestDto));
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleResponseDto> getVehicleById(@PathVariable UUID vehicleId) {
        return ResponseEntity.ok(vehicleService.getVehicleById(vehicleId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<VehicleResponseDto>> getVehiclesByCustomerId(@PathVariable UUID customerId) {
        return ResponseEntity.ok(vehicleService.getVehiclesByCustomerId(customerId));
    }

    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> deleteVehicle(@PathVariable UUID vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
        return ResponseEntity.noContent().build();
    }

}
