package com.Payment_Service.service;

import com.Payment_Service.dto.PaymentRequestDto;
import com.Payment_Service.dto.PaymentResponseDto;
import com.Payment_Service.dto.PaymentStatusUpdateRequest;
import com.Payment_Service.exception.BookingNotFoundException;

import java.util.UUID;

public interface PaymentService {
    PaymentResponseDto createBookingPayment(PaymentRequestDto paymentRequestDto);
    PaymentResponseDto getPaymentById(UUID paymentId);
    PaymentResponseDto getPaymentByBookingId(UUID bookingId) throws BookingNotFoundException;
    PaymentResponseDto verifyPayment(UUID paymentId);
    PaymentResponseDto updatePaymentStatus(UUID paymentId, PaymentStatusUpdateRequest request);
}
