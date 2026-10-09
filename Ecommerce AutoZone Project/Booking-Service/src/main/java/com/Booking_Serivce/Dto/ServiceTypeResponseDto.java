package com.Booking_Serivce.Dto;

import com.Booking_Serivce.Constant.ServiceCategory;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ServiceTypeResponseDto {

    private UUID serviceId;
    private String name;
    private String description;
    private Integer estimatedDurationMinutes;
    private ServiceCategory category;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BigDecimal price;
}