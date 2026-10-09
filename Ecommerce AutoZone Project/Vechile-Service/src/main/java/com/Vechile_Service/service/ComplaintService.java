package com.Vechile_Service.service;

import com.Vechile_Service.dto.ComplaintRequestDto;
import com.Vechile_Service.dto.ComplaintResponseDto;

import java.util.List;
import java.util.UUID;

public interface ComplaintService {
    ComplaintResponseDto registerComplaint(ComplaintRequestDto complaintRequest);
    ComplaintResponseDto getComplaintById(UUID complaintId);
    List<ComplaintResponseDto> getComplaintsByVisitId(UUID vehicleVisitId);
    void deleteComplaint(UUID complaintId);
}
