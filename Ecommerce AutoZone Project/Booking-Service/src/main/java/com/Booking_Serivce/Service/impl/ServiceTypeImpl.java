package com.Booking_Serivce.Service.impl;

import com.Booking_Serivce.Constant.EntityStatus;
import com.Booking_Serivce.Dto.ServiceTypeRequestDto;
import com.Booking_Serivce.Dto.ServiceTypeResponseDto;
import com.Booking_Serivce.Dto.ServiceTypeUpdateDto;
import com.Booking_Serivce.Entity.ServiceType;
import com.Booking_Serivce.Exception.ExceptionClass.ResourceNotFoundException;
import com.Booking_Serivce.Mapper.ServiceTypeMapper;
import com.Booking_Serivce.Repository.ServiceRepository;
import com.Booking_Serivce.Service.ServicesType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceTypeImpl implements ServicesType {

    private final ServiceRepository serviceRepository;
    private final ServiceTypeMapper serviceTypeMapper;

    public ServiceTypeImpl(ServiceRepository serviceRepository, ServiceTypeMapper serviceTypeMapper) {
        this.serviceRepository = serviceRepository;
        this.serviceTypeMapper = serviceTypeMapper;
    }

    @Override
    @Transactional
    public ServiceTypeResponseDto createService(ServiceTypeRequestDto serviceRequestDto) {
        ServiceType serviceType = serviceTypeMapper.toEntity(serviceRequestDto);
        serviceType = serviceRepository.save(serviceType);
        return serviceTypeMapper.toResponseDto(serviceType);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ServiceTypeResponseDto> getAllServices() {
        List<ServiceType> serviceTypes = serviceRepository.findByStatus(EntityStatus.ACTIVE);
        return serviceTypes.stream()
                .map(serviceTypeMapper::toResponseDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ServiceTypeResponseDto getServiceById(String serviceId) {
        ServiceType serviceType = serviceRepository.findById(UUID.fromString(serviceId))
                .orElseThrow(() -> new ResourceNotFoundException("Service Type Not Found with this id " + serviceId ));

        return serviceTypeMapper.toResponseDto(serviceType);
    }

    @Override
    @Transactional
    public ServiceTypeResponseDto updateService(String serviceId, ServiceTypeUpdateDto serviceUpdateDto) {
        ServiceType serviceType = serviceRepository.findById(UUID.fromString(serviceId))
                .orElseThrow(() -> new ResourceNotFoundException("Service Type Not Found with this id " + serviceId ));
        serviceTypeMapper.updateEntityFromDto(serviceUpdateDto, serviceType);
        serviceRepository.save(serviceType);
        return serviceTypeMapper.toResponseDto(serviceType);
    }
}
