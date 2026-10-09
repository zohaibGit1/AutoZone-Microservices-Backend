package com.Booking_Serivce.Client;

import com.Booking_Serivce.Dto.CustomerResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        name="${services.customer-vehicle-service.name}",
        contextId = "customerVehicleClient"
)
public interface CustomerClient {

    @GetMapping("/api/v1/customers/{customerId}")
    CustomerResponseDto getCustomerById(@PathVariable UUID customerId);

}
