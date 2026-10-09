package com.Booking_Serivce.Dto;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponseDto {
    private UUID customerId;
    private String customerName;
    private String customerEmail;
    private String customerPhone;
}
