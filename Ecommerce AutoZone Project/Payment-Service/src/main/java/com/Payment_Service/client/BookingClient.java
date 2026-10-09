package com.Payment_Service.client;

import com.Payment_Service.dto.BookingResponseDto;
import com.Payment_Service.dto.CustomerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "${services.booking-service.name}")
public interface BookingClient {

    @GetMapping("/{bookingId}")
    BookingResponseDto getBookingById(@PathVariable UUID bookingId);
}
