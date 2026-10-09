package com.Booking_Serivce.Mapper;

import com.Booking_Serivce.Dto.GarageRequestDto;
import com.Booking_Serivce.Dto.GarageResponseDto;
import com.Booking_Serivce.Dto.GarageUpdateDto;
import com.Booking_Serivce.Entity.Garage;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface GarageMapper {

    @Mapping(target = "garageId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Garage toEntity(GarageRequestDto dto);

    GarageResponseDto toResponseDto(Garage entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "garageId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(GarageUpdateDto dto, @MappingTarget Garage entity);

    /*
    @MappingTarget Garage entity — iska matlab: naya object return nahi karega, balki existing entity ko modify karega jo tumne pass kiya.
    nullValuePropertyMappingStrategy = IGNORE — ye sabse zaroori
    line hai. Agar GarageUpdateDto mein koi field null hai
    (customer ne update nahi karna chaha), to us field ko overwrite mat karo,
    existing value waise hi chhod do.
     */

}
