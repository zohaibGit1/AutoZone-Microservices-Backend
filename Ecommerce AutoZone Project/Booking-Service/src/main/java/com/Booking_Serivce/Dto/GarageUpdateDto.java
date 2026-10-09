package com.Booking_Serivce.Dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class GarageUpdateDto {

    @Size(max = 50, message = "Garage name must not exceed 50 characters")
    private String garageName;

    @Size(max = 100, message = "Address must not exceed 100 characters")
    private String address;

    @Size(max = 50, message = "Contact info must not exceed 50 characters")
    private String contactInfo;

    @Min(value = 1, message = "Capacity must be at least 1")
    private Integer capacity;

    private LocalTime openingTime;

    private LocalTime closingTime;

    @Size(max = 20, message = "Weekly off must not exceed 20 characters")
    private String weeklyOff;

    private String status;
}