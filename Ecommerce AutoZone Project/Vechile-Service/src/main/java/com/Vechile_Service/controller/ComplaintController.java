package com.Vechile_Service.controller;

import com.Vechile_Service.dto.ComplaintRequestDto;
import com.Vechile_Service.dto.ComplaintResponseDto;
import com.Vechile_Service.service.ComplaintService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/complaint")
@RequiredArgsConstructor
public class ComplaintController {

    private final ComplaintService complaintService;

    @PostMapping({"", "/register/complaint"})
    public ResponseEntity<ComplaintResponseDto> registerComplaint(@Valid @RequestBody ComplaintRequestDto complaintRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(complaintService.registerComplaint(complaintRequest));
    }

    @GetMapping("/{complaintId}")
    public ResponseEntity<ComplaintResponseDto> getComplaintById(@PathVariable UUID complaintId) {
        return ResponseEntity.ok(complaintService.getComplaintById(complaintId));
    }

    @GetMapping("/visit/{vehicleVisitId}")
    public ResponseEntity<List<ComplaintResponseDto>> getComplaintsByVisitId(@PathVariable UUID vehicleVisitId) {
        return ResponseEntity.ok(complaintService.getComplaintsByVisitId(vehicleVisitId));
    }

    @DeleteMapping("/{complaintId}")
    public ResponseEntity<Void> deleteComplaint(@PathVariable UUID complaintId) {
        complaintService.deleteComplaint(complaintId);
        return ResponseEntity.noContent().build();
    }

}
