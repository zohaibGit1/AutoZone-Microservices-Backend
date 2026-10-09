package com.Payment_Service.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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