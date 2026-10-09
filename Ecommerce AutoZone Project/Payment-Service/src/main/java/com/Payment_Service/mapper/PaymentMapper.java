package com.Payment_Service.mapper;

import com.Payment_Service.dto.PaymentResponseDto;
import com.Payment_Service.entity.Payment;

public interface PaymentMapper {

    PaymentResponseDto toPaymentResponseDto(Payment payment);

    Payment toEntity(PaymentResponseDto paymentResponseDto);


}
