package com.Booking_Serivce.Service;

import com.Booking_Serivce.Dto.ServiceTypeRequestDto;
import com.Booking_Serivce.Dto.ServiceTypeResponseDto;
import com.Booking_Serivce.Dto.ServiceTypeUpdateDto;

import java.util.List;

public interface ServicesType {
    ServiceTypeResponseDto createService(ServiceTypeRequestDto serviceRequestDto);
    List<ServiceTypeResponseDto> getAllServices();
    ServiceTypeResponseDto getServiceById(String serviceId);
    ServiceTypeResponseDto updateService(String serviceId, ServiceTypeUpdateDto serviceUpdateDto);
}
