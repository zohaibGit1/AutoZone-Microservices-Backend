package com.Booking_Serivce.Dto;

import com.Booking_Serivce.Constant.ServiceCategory;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ServiceTypeUpdateDto {

    @Size(max = 100, message = "Service name must not exceed 100 characters")
    private String name;

    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @Min(value = 1, message = "Estimated duration must be at least 1 minute")
    private Integer estimatedDurationMinutes;

    private ServiceCategory category;

    private String status;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be non-negative")
    private BigDecimal price;
}