package com.Payment_Service.service.impl;

import com.Payment_Service.dto.GatewayOrderResponseDto;
import com.Payment_Service.service.RazorPayGatewayService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RazorPayGatewayServiceImpl implements RazorPayGatewayService {

    private final RazorpayClient razorpayClient;

    @Override
    public GatewayOrderResponseDto createOrder(UUID bookingId, BigDecimal amount) {
        // Jo Object hmlog Create Krte hai RazorPay k pass json object mei jaata hai
        try {
            JSONObject options = new JSONObject();
            options.put("amount", amount.multiply(new BigDecimal(100)).longValue());
            options.put("currency", "INR");
            options.put("receipt", bookingId.toString());
            options.put("notes", new JSONObject().put("bookingId", bookingId.toString()));
            // Most Important line using below help the razorClient to create the object
            Order order = razorpayClient.orders.create(options);
            return new GatewayOrderResponseDto(
                    order.get("id"),
                    order.get("currency"),
                    amount,
                    order.get("status"),
                    "RAZORPAY"
            );
        } catch (RazorpayException e) {
            throw new IllegalStateException("Razor order create failed ", e);
        }
    }
}
