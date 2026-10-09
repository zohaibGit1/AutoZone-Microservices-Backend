package com.Payment_Service.client;

import com.Payment_Service.dto.CustomerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
    name = "${services.customer-vehicle-service.name}"
)
public interface CustomerClient {
    @GetMapping("/api/v1/customers/{customerId}")
    CustomerResponseDto getCustomerById(
            @PathVariable UUID customerId
    );
}
