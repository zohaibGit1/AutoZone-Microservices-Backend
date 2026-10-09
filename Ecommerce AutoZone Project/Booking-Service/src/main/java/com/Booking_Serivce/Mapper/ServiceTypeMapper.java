package com.Booking_Serivce.Mapper;

import com.Booking_Serivce.Dto.ServiceTypeRequestDto;
import com.Booking_Serivce.Dto.ServiceTypeResponseDto;
import com.Booking_Serivce.Dto.ServiceTypeUpdateDto;
import com.Booking_Serivce.Entity.ServiceType;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ServiceTypeMapper {

    @Mapping(target = "serviceId", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ServiceType toEntity(ServiceTypeRequestDto serviceTypeRequestDto);

    ServiceTypeResponseDto toResponseDto(ServiceType serviceType);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "serviceId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(ServiceTypeUpdateDto serviceTypeUpdateDto, @MappingTarget ServiceType entity);


}
