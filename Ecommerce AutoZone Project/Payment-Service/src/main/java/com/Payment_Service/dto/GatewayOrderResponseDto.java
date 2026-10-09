package com.Payment_Service.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GatewayOrderResponseDto {
    private String gatewayId;
    private String paymentStatus;
    private BigDecimal amount;
    private String currency;
    private String gatewayName;
}
