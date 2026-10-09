package com.Payment_Service.dto;

import com.Payment_Service.constant.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentStatusUpdateRequest {

    @NotNull
    private PaymentStatus paymentStatus;
}
