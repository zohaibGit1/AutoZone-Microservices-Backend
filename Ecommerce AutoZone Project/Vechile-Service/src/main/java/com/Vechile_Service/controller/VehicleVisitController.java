package com.Vechile_Service.controller;

import com.Vechile_Service.dto.VehicleVisitRequestDto;
import com.Vechile_Service.dto.VehicleVisitResponse;
import com.Vechile_Service.service.VehicleVisitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/vehicle-visit")
@RequiredArgsConstructor
public class VehicleVisitController {

    private final VehicleVisitService vehicleVisitService;

    @PostMapping
    public ResponseEntity<VehicleVisitResponse> registerVehicleVisit(
            @Valid @RequestBody VehicleVisitRequestDto vehicleVisitRequest) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(vehicleVisitService.registerVehicleVisit(vehicleVisitRequest));
    }

    @GetMapping("/{visitId}")
    public ResponseEntity<VehicleVisitResponse> getVisitById(@PathVariable UUID visitId) {
        return ResponseEntity.ok(vehicleVisitService.getVisitById(visitId));
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<List<VehicleVisitResponse>> getVisitsByVehicleId(@PathVariable UUID vehicleId) {
        return ResponseEntity.ok(vehicleVisitService.getVisitsByVehicleId(vehicleId));
    }

    @DeleteMapping("/{visitId}")
    public ResponseEntity<Void> deleteVehicleVisit(@PathVariable UUID visitId) {
        vehicleVisitService.deleteVehicleVisit(visitId);
        return ResponseEntity.noContent().build();
    }
}
