package com.Vechile_Service.service.impl;

import com.Vechile_Service.dto.VehicleVisitRequestDto;
import com.Vechile_Service.dto.VehicleVisitResponse;
import com.Vechile_Service.entity.Vehicle;
import com.Vechile_Service.entity.VehicleVisit;
import com.Vechile_Service.exception.ResourceNotFoundException;
import com.Vechile_Service.mapper.Mapper;
import com.Vechile_Service.repo.VehicleRepository;
import com.Vechile_Service.repo.VehicleVisitRepository;
import com.Vechile_Service.service.VehicleVisitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleVisitServiceImpl implements VehicleVisitService {

    private final Mapper mapper;
    private final VehicleVisitRepository vehicleVisitRepository;
    private final VehicleRepository vehicleRepository;


    @Override
    @Transactional
    public VehicleVisitResponse registerVehicleVisit(VehicleVisitRequestDto vehicleVisitRequest) {
        log.info("Registering vehicle visit for vehicle: {}", vehicleVisitRequest.getVehicleId());
        Vehicle vehicle = vehicleRepository.findById(vehicleVisitRequest.getVehicleId()).orElseThrow(() ->
                new ResourceNotFoundException("Vehicle not found with the id " + vehicleVisitRequest.getVehicleId()));

        VehicleVisit vehicleVisit = VehicleVisit.builder()
                .currentKm(vehicleVisitRequest.getCurrentKm())
                .vehicle(vehicle)
                .bookingId(vehicleVisitRequest.getBookingId())
                .build();

        VehicleVisit saved = vehicleVisitRepository.save(vehicleVisit);
        VehicleVisitResponse response = mapper.toResponseDto(saved);
        log.info("Successfully registered vehicle visit with ID: {}", response.getVisitId());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleVisitResponse getVisitById(UUID visitId) {
        VehicleVisit visit = vehicleVisitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle visit not found with id " + visitId));
        return mapper.toResponseDto(visit);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleVisitResponse> getVisitsByVehicleId(UUID vehicleId) {
        return vehicleVisitRepository.findByVehicleVehicleId(vehicleId).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteVehicleVisit(UUID visitId) {
        log.info("Deleting vehicle visit with ID: {}", visitId);
        VehicleVisit visit = vehicleVisitRepository.findById(visitId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle visit not found with id " + visitId));
        vehicleVisitRepository.delete(visit);
        log.info("Successfully deleted vehicle visit with ID: {}", visitId);
    }
}
