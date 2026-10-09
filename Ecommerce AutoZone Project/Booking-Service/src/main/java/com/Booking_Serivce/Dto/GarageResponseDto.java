package com.Booking_Serivce.Dto;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GarageResponseDto {

    private UUID garageId;
    private String garageName;
    private String address;
    private String contactInfo;
    private Integer capacity;
    private LocalTime openingTime;
    private LocalTime closingTime;
    private String weeklyOff;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}