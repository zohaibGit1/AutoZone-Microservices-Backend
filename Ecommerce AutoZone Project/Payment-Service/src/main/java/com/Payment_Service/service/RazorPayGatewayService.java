package com.Payment_Service.service;

import com.Payment_Service.dto.GatewayOrderResponseDto;

import java.math.BigDecimal;
import java.util.UUID;

public interface RazorPayGatewayService {

    GatewayOrderResponseDto createOrder(UUID bookingId, BigDecimal amount);

}
