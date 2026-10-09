package com.Booking_Serivce.Mapper;

import com.Booking_Serivce.Dto.BookingRequestDto;
import com.Booking_Serivce.Dto.BookingResponseDto;
import com.Booking_Serivce.Entity.Booking;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "bookingId", ignore = true)
    @Mapping(target = "garage", ignore = true)
    @Mapping(target = "serviceType", ignore = true)
    @Mapping(target = "endTime", ignore = true)
    @Mapping(target = "bookingStatus", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Booking toEntity(BookingRequestDto requestDto);

    @Mapping(target = "garageId", source = "garage.garageId")
    @Mapping(target = "serviceId", source = "serviceType.serviceId")
    BookingResponseDto toResponse(Booking booking);

}
