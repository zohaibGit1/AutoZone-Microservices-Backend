package com.Vechile_Service.mapper;

import com.Vechile_Service.dto.*;
import com.Vechile_Service.entity.*;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public CustomerResponseDto toResponse(Customer customer) {
        return CustomerResponseDto.builder()
                .customerId(customer.getCustomerId())
                .customerName(customer.getCustomerName())
                .customerEmail(customer.getCustomerEmail())
                .customerPhone(customer.getCustomerPhone())
                .createdAt(customer.getCreatedAt())
                .updatedAt(customer.getUpdatedAt())
                .vehicles(
                        customer.getVehicles().stream()
                                .map(this::toResponse)
                                .toList()
                )
                .build();
    }

    public VehicleResponseDto toResponse(Vehicle vehicle) {
        return VehicleResponseDto.builder()
                .vehicleId(vehicle.getVehicleId())
                .vehicleModel(vehicle.getVehicleModel())
                .vehicleNumber(vehicle.getVehicleNumber())
                .vehicleName(vehicle.getVehicleName())
                .vehicleType(vehicle.getVehicleType())
                .customerId(vehicle.getCustomer().getCustomerId())
                .build();
    }

    public VehicleVisitResponse toResponseDto(VehicleVisit vehicleVisit) {
        return VehicleVisitResponse.builder()
                .visitId(vehicleVisit.getVisitId())
                .visitDate(vehicleVisit.getVisitDate())
                .currentKm(vehicleVisit.getCurrentKm())
                .checkInTime(vehicleVisit.getCheckInTime())
                .vehicleId(vehicleVisit.getVehicle().getVehicleId())
                .bookingId(vehicleVisit.getBookingId())
                .build();
    }

    public ComplaintResponseDto toResponseDto(Complaint complaint) {
        return ComplaintResponseDto.builder()
                .complaintId(complaint.getComplaintId())
                .complaintDescription(complaint.getComplaintDescription())
                .vehicleVisitId(complaint.getVehicleVisit().getVisitId())
                .build();
    }

    public VehicleVisitHistoryDto toHistoryResponse(VehicleVisit visit) {

        return VehicleVisitHistoryDto.builder()
                .visitId(visit.getVisitId())
                .visitDate(visit.getVisitDate())
                .currentKm(visit.getCurrentKm())
                .vehicleId(visit.getVehicle().getVehicleId())
                .vehicleNumber(visit.getVehicle().getVehicleNumber())
                .complaints(
                        visit.getComplaints()
                                .stream()
                                .map(this::toResponseDto)
                                .toList()
                )
                .build();
    }
}
