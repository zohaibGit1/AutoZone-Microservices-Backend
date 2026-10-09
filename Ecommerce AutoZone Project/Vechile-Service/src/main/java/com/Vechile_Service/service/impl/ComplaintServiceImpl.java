package com.Vechile_Service.service.impl;

import com.Vechile_Service.dto.ComplaintRequestDto;
import com.Vechile_Service.dto.ComplaintResponseDto;
import com.Vechile_Service.entity.Complaint;
import com.Vechile_Service.entity.VehicleVisit;
import com.Vechile_Service.exception.ResourceNotFoundException;
import com.Vechile_Service.mapper.Mapper;
import com.Vechile_Service.repo.ComplaintRepository;
import com.Vechile_Service.repo.VehicleVisitRepository;
import com.Vechile_Service.service.ComplaintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final VehicleVisitRepository vehicleVisitRepository;
    private final Mapper mapper;

    @Override
    @Transactional
    public ComplaintResponseDto registerComplaint(
            ComplaintRequestDto complaintRequest) {
        log.info("Registering complaint for vehicle visit: {}", complaintRequest.getVehicleVisitId());
        VehicleVisit vehicleVisit = vehicleVisitRepository
                .findById(complaintRequest.getVehicleVisitId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Vehicle visit not found with id "
                                        + complaintRequest.getVehicleVisitId()
                        ));

        Complaint complaint = Complaint.builder()
                .complaintDescription(complaintRequest.getComplaintDescription())
                .vehicleVisit(vehicleVisit)
                .build();

        Complaint saved = complaintRepository.save(complaint);
        ComplaintResponseDto response = mapper.toResponseDto(saved);
        log.info("Successfully registered complaint with ID: {}", response.getComplaintId());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public ComplaintResponseDto getComplaintById(UUID complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id " + complaintId));
        return mapper.toResponseDto(complaint);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ComplaintResponseDto> getComplaintsByVisitId(UUID vehicleVisitId) {
        return complaintRepository.findByVehicleVisitVisitId(vehicleVisitId).stream()
                .map(mapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional
    public void deleteComplaint(UUID complaintId) {
        log.info("Deleting complaint with ID: {}", complaintId);
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new ResourceNotFoundException("Complaint not found with id " + complaintId));
        complaintRepository.delete(complaint);
        log.info("Successfully deleted complaint with ID: {}", complaintId);
    }
}
