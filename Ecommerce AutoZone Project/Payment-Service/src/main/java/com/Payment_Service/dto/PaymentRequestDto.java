package com.Payment_Service.dto;

import com.Payment_Service.constant.PaymentMethod;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDto {

    @NotNull
    private UUID bookingId;

    @NotNull
    private UUID customerId;

    @NotNull
    @Positive
    private BigDecimal amount;

    @NotNull
    private PaymentMethod paymentMethod;
}
