package com.Booking_Serivce.Service.impl;

import com.Booking_Serivce.Constant.EntityStatus;
import com.Booking_Serivce.Dto.GarageRequestDto;
import com.Booking_Serivce.Dto.GarageResponseDto;
import com.Booking_Serivce.Dto.GarageUpdateDto;
import com.Booking_Serivce.Entity.Garage;
import com.Booking_Serivce.Exception.ExceptionClass.ResourceNotFoundException;
import com.Booking_Serivce.Mapper.GarageMapper;
import com.Booking_Serivce.Repository.GarageRepository;
import com.Booking_Serivce.Service.GarageService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GarageServiceImpl implements GarageService {

    private final GarageRepository garageRepository;
    private final GarageMapper garageMapper;

    public GarageServiceImpl(GarageRepository garageRepository, GarageMapper garageMapper) {
        this.garageRepository = garageRepository;
        this.garageMapper = garageMapper;
    }

    @Override
    @Transactional
    public GarageResponseDto createGarage(GarageRequestDto garageRequestDto) {
        Garage garage = garageMapper.toEntity(garageRequestDto);
        garage = garageRepository.save(garage);
        return garageMapper.toResponseDto(garage);
    }

    @Override
    @Transactional
    public GarageResponseDto updateGarage(String garageId, GarageUpdateDto garageUpdateDto) {
        Garage existingGarage = garageRepository.findById(UUID.fromString(garageId))
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found"));
        garageMapper.updateEntityFromDto(garageUpdateDto, existingGarage);
        Garage saved = garageRepository.save(existingGarage);
        return garageMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public GarageResponseDto getGarageById(String garageId) {
        Garage existingGarage = garageRepository.findById(UUID.fromString(garageId))
                .orElseThrow(() -> new ResourceNotFoundException("Garage not found"));
        return garageMapper.toResponseDto(existingGarage);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GarageResponseDto> getAllGarages() {
        List<Garage> garages =  garageRepository.findByStatus(EntityStatus.ACTIVE);
        return garages.stream()
                .map(garageMapper::toResponseDto)
                .collect(Collectors.toList());
    }

}
