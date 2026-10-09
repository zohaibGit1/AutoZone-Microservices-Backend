package com.Payment_Service.mapper.impl;

import com.Payment_Service.dto.PaymentResponseDto;
import com.Payment_Service.entity.Payment;
import com.Payment_Service.mapper.PaymentMapper;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapperImpl implements PaymentMapper {

    @Override
    public PaymentResponseDto toPaymentResponseDto(Payment payment) {
        if (payment == null) {
            return null;
        }
        return PaymentResponseDto.builder()
                .paymentId(payment.getPaymentId())
                .bookingId(payment.getBookingId())
                .customerId(payment.getCustomerId())
                .amount(payment.getAmount())
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .transactionId(payment.getTransactionId())
                .build();
    }

    @Override
    public Payment toEntity(PaymentResponseDto paymentResponseDto) {
        if (paymentResponseDto == null) {
            return null;
        }
        return Payment.builder()
                .paymentId(paymentResponseDto.getPaymentId())
                .bookingId(paymentResponseDto.getBookingId())
                .customerId(paymentResponseDto.getCustomerId())
                .amount(paymentResponseDto.getAmount())
                .paymentMethod(paymentResponseDto.getPaymentMethod())
                .paymentStatus(paymentResponseDto.getPaymentStatus())
                .transactionId(paymentResponseDto.getTransactionId())
                .build();
    }
}
