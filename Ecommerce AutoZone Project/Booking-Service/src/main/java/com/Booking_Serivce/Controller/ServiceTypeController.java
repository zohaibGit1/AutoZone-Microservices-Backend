package com.Booking_Serivce.Controller;

import com.Booking_Serivce.Dto.ServiceTypeRequestDto;
import com.Booking_Serivce.Dto.ServiceTypeResponseDto;
import com.Booking_Serivce.Dto.ServiceTypeUpdateDto;
import com.Booking_Serivce.Service.ServicesType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceTypeController {

    private final ServicesType servicesType;

    @PostMapping
    public ResponseEntity<ServiceTypeResponseDto> createService(
            @Valid @RequestBody ServiceTypeRequestDto serviceRequestDto
    ) {
        ServiceTypeResponseDto response = servicesType.createService(serviceRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ServiceTypeResponseDto>> getAllServices() {
        List<ServiceTypeResponseDto> response = servicesType.getAllServices();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceTypeResponseDto> getServiceById(
            @PathVariable String serviceId
    ) {
        ServiceTypeResponseDto response = servicesType.getServiceById(serviceId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{serviceId}")
    public ResponseEntity<ServiceTypeResponseDto> updateService(
            @PathVariable String serviceId,
            @Valid @RequestBody ServiceTypeUpdateDto serviceUpdateDto
    ) {
        ServiceTypeResponseDto response = servicesType.updateService(serviceId, serviceUpdateDto);
        return ResponseEntity.ok(response);
    }
}