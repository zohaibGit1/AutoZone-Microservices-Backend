package com.Payment_Service.service.impl;
import com.Payment_Service.client.BookingClient;
import com.Payment_Service.constant.PaymentStatus;
import com.Payment_Service.dto.GatewayOrderResponseDto;
import com.Payment_Service.dto.PaymentRequestDto;
import com.Payment_Service.dto.PaymentResponseDto;
import com.Payment_Service.dto.PaymentStatusUpdateRequest;
import com.Payment_Service.entity.Payment;
import com.Payment_Service.exception.BookingNotFoundException;
import com.Payment_Service.exception.PaymentNotFoundException;
import com.Payment_Service.mapper.PaymentMapper;
import com.Payment_Service.repository.PaymentRepository;
import com.Payment_Service.service.PaymentService;
import com.Payment_Service.service.RazorPayGatewayService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentMapper paymentMapper;
    private final BookingClient bookingClient;
    private final PaymentRepository paymentRepository;
    private final RazorPayGatewayService razorPayGatewayService;

    @Override
    public PaymentResponseDto createBookingPayment(PaymentRequestDto paymentRequestDto) {
        GatewayOrderResponseDto order =
                razorPayGatewayService.createOrder(
                        paymentRequestDto.getBookingId(),
                        paymentRequestDto.getAmount()
                );
        Payment payment = Payment.builder()
                .bookingId(paymentRequestDto.getBookingId())
                .customerId(paymentRequestDto.getCustomerId())
                .amount(paymentRequestDto.getAmount())
                .paymentMethod(paymentRequestDto.getPaymentMethod())
                .paymentStatus(PaymentStatus.PENDING)
                .razorpayOrderId(order.getGatewayId())
                .build();
        return paymentMapper.toPaymentResponseDto(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponseDto getPaymentById(UUID paymentId) {
        UUID id = UUID.fromString(paymentId.toString());
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new PaymentNotFoundException("Payment Not Found With the " + id));
        return paymentMapper.toPaymentResponseDto(payment);
    }

    @Override
    public PaymentResponseDto getPaymentByBookingId(UUID bookingId) throws BookingNotFoundException {
        try {
            bookingClient.getBookingById(UUID.fromString(bookingId.toString()));
            UUID id = UUID.fromString(bookingId.toString());
            Payment payment = paymentRepository.findByBookingId(id)
                    .orElseThrow(() -> new PaymentNotFoundException("Booking Payment Not Found With the " + id));
            return paymentMapper.toPaymentResponseDto(payment);
        }catch (FeignException.NotFound e) {
            throw new BookingNotFoundException("Booking Not Found With the " + bookingId);
        }
    }

    // TODO: CHECK PAYMENT SUCCESS THEN SEND PAYMENT ID TO BOOKING SERVICE
    @Override
    public PaymentResponseDto verifyPayment(UUID paymentId) {
        UUID id = UUID.fromString(paymentId.toString());
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment Not Found With the " + id));
        return null;
    }

    @Override
    public PaymentResponseDto updatePaymentStatus(UUID paymentId, PaymentStatusUpdateRequest request) {
        UUID id = UUID.fromString(paymentId.toString());
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment Not Found With the " + id));
        payment.setPaymentStatus(request.getPaymentStatus());
        return paymentMapper.toPaymentResponseDto(payment);
    }
}