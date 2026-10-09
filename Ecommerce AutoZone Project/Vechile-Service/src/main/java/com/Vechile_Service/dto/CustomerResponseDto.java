package com.Vechile_Service.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerResponseDto {
    private UUID customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<VehicleResponseDto> vehicles;

}
