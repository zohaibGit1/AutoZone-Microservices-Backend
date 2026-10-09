package com.Payment_Service.dto;

import com.Payment_Service.constant.PaymentMethod;
import com.Payment_Service.constant.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

    private UUID paymentId;
    private UUID bookingId;
    private UUID customerId;
    private BigDecimal amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private String transactionId;
}