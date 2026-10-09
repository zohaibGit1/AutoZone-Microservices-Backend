package com.Vechile_Service.service;

import com.Vechile_Service.dto.VehicleVisitRequestDto;
import com.Vechile_Service.dto.VehicleVisitResponse;

import java.util.List;
import java.util.UUID;

public interface VehicleVisitService {
    VehicleVisitResponse registerVehicleVisit(VehicleVisitRequestDto vehicleVisit);
    VehicleVisitResponse getVisitById(UUID visitId);
    List<VehicleVisitResponse> getVisitsByVehicleId(UUID vehicleId);
    void deleteVehicleVisit(UUID visitId);
}
