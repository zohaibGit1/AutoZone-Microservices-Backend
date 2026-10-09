package com.Payment_Service.controller;

import com.Payment_Service.service.impl.RazorPaymentWebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhook")
@RequiredArgsConstructor
public class WebhookController {

    private final RazorPaymentWebhookService paymentWebhookService;

    @PostMapping("/razorpay")
    public ResponseEntity<?> handleWebhook(@RequestHeader("X-Razorpay-Signature") String signature, @RequestBody String payload) {
        paymentWebhookService.processWebhook(signature, payload);
        return ResponseEntity.ok().build();
    }

}
