package com.Vechile_Service.service;

import com.Vechile_Service.dto.CustomerHistoryDto;
import com.Vechile_Service.dto.CustomerRequestDto;
import com.Vechile_Service.dto.CustomerResponseDto;
import com.Vechile_Service.dto.CustomerUpdateDto;

import java.util.UUID;

public interface CustomerService {
    CustomerResponseDto registerCustomer(CustomerRequestDto requestDto);
    CustomerHistoryDto getCustomerVehicleHistory(String search);
    CustomerResponseDto updateCustomer(UUID customerId, CustomerUpdateDto updateDto);
    CustomerResponseDto getCustomerById(UUID customerId);
    void deleteCustomer(UUID customerId);
}
