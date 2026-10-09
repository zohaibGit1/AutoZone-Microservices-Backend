package com.Booking_Serivce.Dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvailableSlotDto {

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private int availableCapacity;
}