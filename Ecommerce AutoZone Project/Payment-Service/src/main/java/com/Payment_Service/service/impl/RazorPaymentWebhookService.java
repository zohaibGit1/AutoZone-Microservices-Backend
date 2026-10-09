package com.Payment_Service.service.impl;

import com.Payment_Service.client.BookingClient;
import com.Payment_Service.config.RazorpayConfig;
import com.Payment_Service.constant.PaymentStatus;
import com.Payment_Service.entity.Payment;
import com.Payment_Service.mapper.PaymentMapper;
import com.Payment_Service.repository.PaymentRepository;
import com.razorpay.RazorpayException;
import com.razorpay.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RazorPaymentWebhookService {
    private final RazorpayConfig razorpayConfig;
    private final PaymentMapper paymentMapper;
    private final BookingClient bookingClient;
    private final PaymentRepository paymentRepository;

    @Value("${razorpay.webhook.secret}")
    private String webhooksSecret;

    public void processWebhook(String signature, String payload) {
        verifyPayment(signature, payload);
        JSONObject event = new JSONObject(payload);
        String eventType = event.has("event") ? event.getString("event") : event.optString("events");
        log.info("Received Razorpay Webhook event: {}", eventType);

        switch (eventType) {
            case "payment.captured", "order.paid" -> handlePaymentSuccess(event);
            case "payment.failed", "payment.failure" -> handlePaymentFailure(event);
            default -> log.warn("Unhandled Razorpay event: {}", eventType);
        }
    }

    private void handlePaymentSuccess(JSONObject event) {
        JSONObject paymentEntity = extractPaymentEntity(event);
        String razorpayOrderId = paymentEntity.optString("order_id");
        String razorpayPaymentId = paymentEntity.optString("id");

        log.info("Handling payment success: orderId={}, paymentId={}", razorpayOrderId, razorpayPaymentId);

        findPayment(razorpayOrderId, razorpayPaymentId).ifPresentOrElse(
                payment -> {
                    payment.setPaymentStatus(PaymentStatus.SUCCESS);
                    if (razorpayPaymentId != null && !razorpayPaymentId.isBlank()) {
                        payment.setTransactionId(razorpayPaymentId);
                    }
                    paymentRepository.save(payment);
                    log.info("Payment marked as SUCCESS for bookingId={}, paymentId={}",
                            payment.getBookingId(), payment.getPaymentId());
                },
                () -> log.warn("Payment not found for success webhook: orderId={}, paymentId={}",
                        razorpayOrderId, razorpayPaymentId)
        );
    }

    private void handlePaymentFailure(JSONObject event) {
        JSONObject paymentEntity = extractPaymentEntity(event);
        String razorpayOrderId = paymentEntity.optString("order_id");
        String razorpayPaymentId = paymentEntity.optString("id");
        String errorDescription = paymentEntity.optString("error_description", "Payment failed");

        log.warn("Handling payment failure: orderId={}, paymentId={}, reason={}",
                razorpayOrderId, razorpayPaymentId, errorDescription);

        findPayment(razorpayOrderId, razorpayPaymentId).ifPresentOrElse(
                payment -> {
                    payment.setPaymentStatus(PaymentStatus.FAILED);
                    if (razorpayPaymentId != null && !razorpayPaymentId.isBlank()) {
                        payment.setTransactionId(razorpayPaymentId);
                    }
                    paymentRepository.save(payment);
                    log.info("Payment marked as FAILED for bookingId={}, paymentId={}",
                            payment.getBookingId(), payment.getPaymentId());
                },
                () -> log.warn("Payment not found for failure webhook: orderId={}, paymentId={}",
                        razorpayOrderId, razorpayPaymentId)
        );
    }

    private Optional<Payment> findPayment(String orderId, String paymentId) {
        if (orderId != null && !orderId.isBlank()) {
            Optional<Payment> payment =
                    paymentRepository.findByRazorpayOrderId(orderId);
            if (payment.isPresent()) {
                return payment;
            }
        }
        if (paymentId != null && !paymentId.isBlank()) {
            return paymentRepository.findByTransactionId(paymentId);
        }
        return Optional.empty();
    }

    private JSONObject extractPaymentEntity(JSONObject event) {
        return event.getJSONObject("payload")
                .getJSONObject("payment")
                .getJSONObject("entity");
    }


    private void verifyPayment(String signature, String payload) {
        try {
            Utils.verifySignature(payload, signature, webhooksSecret);
        } catch (RazorpayException e) {
            log.error("Invalid  Razorpay webhook signature");
            throw new SecurityException("Invalid Razorpay webhook signature");
        }
    }
}
