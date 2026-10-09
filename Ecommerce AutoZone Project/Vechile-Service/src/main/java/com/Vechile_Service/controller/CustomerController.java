package com.Vechile_Service.controller;

import com.Vechile_Service.dto.CustomerHistoryDto;
import com.Vechile_Service.dto.CustomerRequestDto;
import com.Vechile_Service.dto.CustomerResponseDto;
import com.Vechile_Service.dto.CustomerUpdateDto;
import com.Vechile_Service.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping({"", "/register-customer"})
    public ResponseEntity<CustomerResponseDto> registerCustomer(@Valid @RequestBody CustomerRequestDto requestDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(customerService.registerCustomer(requestDto));
    }

    @GetMapping("/history")
    public ResponseEntity<CustomerHistoryDto> getCustomerVehicleHistory(
            @RequestParam String search) {
        return ResponseEntity.ok(
                customerService.getCustomerVehicleHistory(search));
    }

    @PatchMapping({"/{customerId}", "/update-customer/{customerId}"})
    public ResponseEntity<CustomerResponseDto> updateCustomer(
            @PathVariable UUID customerId,
            @Valid @RequestBody CustomerUpdateDto updateDto) {
        log.info("Customer Name, Customer Phone Number ,{}, {}", updateDto.getCustomerName(), updateDto.getCustomerPhone());
        return ResponseEntity.ok(customerService.updateCustomer(customerId, updateDto));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<CustomerResponseDto> getCustomerById(@PathVariable UUID customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

}
