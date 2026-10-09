package com.Booking_Serivce.Dto;


import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailabilityResponseDto {

    private UUID garageId;

    private UUID serviceId;

    private LocalDate date;

    private List<AvailableSlotDto> availableSlots;
}