package com.Booking_Serivce.Service;

import com.Booking_Serivce.Dto.GarageRequestDto;
import com.Booking_Serivce.Dto.GarageResponseDto;
import com.Booking_Serivce.Dto.GarageUpdateDto;

import java.util.List;

public interface GarageService {
    GarageResponseDto createGarage(GarageRequestDto garageRequestDto);
    GarageResponseDto updateGarage(String garageId, GarageUpdateDto garageUpdateDto);
    GarageResponseDto getGarageById(String garageId);
    List<GarageResponseDto> getAllGarages();
}
