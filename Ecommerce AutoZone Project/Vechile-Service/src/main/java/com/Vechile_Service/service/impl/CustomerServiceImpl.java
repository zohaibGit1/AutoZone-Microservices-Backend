package com.Vechile_Service.service.impl;

import com.Vechile_Service.dto.CustomerHistoryDto;
import com.Vechile_Service.dto.CustomerRequestDto;
import com.Vechile_Service.dto.CustomerResponseDto;
import com.Vechile_Service.dto.CustomerUpdateDto;
import com.Vechile_Service.entity.Customer;
import com.Vechile_Service.entity.VehicleVisit;
import com.Vechile_Service.exception.DuplicateResourceException;
import com.Vechile_Service.exception.ResourceNotFoundException;
import com.Vechile_Service.mapper.Mapper;
import com.Vechile_Service.repo.CustomerRepository;
import com.Vechile_Service.repo.VehicleVisitRepository;
import com.Vechile_Service.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {

        private final CustomerRepository customerRepository;
        private final Mapper customerMapper;
        private final VehicleVisitRepository vehicleVisitRepository;

        @Override
        @Transactional
        public CustomerResponseDto registerCustomer(CustomerRequestDto requestDto) {
                log.info("Registering new customer with email: {}", requestDto.getCustomerEmail());
                String normalizedEmail = requestDto.getCustomerEmail().trim().toLowerCase(Locale.ROOT);
                String normalizedPhone = requestDto.getCustomerPhone().trim();

                if (customerRepository.existsByCustomerEmailIgnoreCase(normalizedEmail)) {
                        throw new DuplicateResourceException("A customer already exists with email " + normalizedEmail);
                }
                if (customerRepository.existsByCustomerPhone(normalizedPhone)) {
                        throw new DuplicateResourceException("A customer already exists with phone " + normalizedPhone);
                }

                Customer customer = Customer.builder()
                                .customerName(requestDto.getCustomerName().trim())
                                .customerEmail(normalizedEmail)
                                .customerPhone(normalizedPhone)
                                .build();
                CustomerResponseDto response = customerMapper.toResponse(customerRepository.save(customer));
                log.info("Successfully registered customer with ID: {}", response.getCustomerId());
                return response;
        }

        @Override
        @Transactional(readOnly = true)
        public CustomerHistoryDto getCustomerVehicleHistory(String search) {

                String normalizedSearch = search == null ? "" : search.trim();
                Optional<Customer> customer;

                if (normalizedSearch.contains("@")) {
                        customer = customerRepository.findByCustomerEmailIgnoreCase(normalizedSearch);
                } else {
                        String normalizedPhone = normalizedSearch.replaceAll("\\D", "");
                        if (normalizedPhone.length() == 12 && normalizedPhone.startsWith("91")) {
                                normalizedPhone = normalizedPhone.substring(2);
                        }
                        customer = customerRepository.findByCustomerPhone(normalizedPhone);
                }

                Customer customerData = customer.orElseThrow(
                                () -> new ResourceNotFoundException("Customer not found " + normalizedSearch));

                List<VehicleVisit> visits = vehicleVisitRepository.findByVehicleCustomerCustomerId(
                                customerData.getCustomerId());

                return CustomerHistoryDto.builder()
                                .customerId(customerData.getCustomerId())
                                .customerName(customerData.getCustomerName())
                                .customerEmail(customerData.getCustomerEmail())
                                .customerPhone(customerData.getCustomerPhone())
                                .vehicles(
                                                customerData.getVehicles()
                                                                .stream()
                                                                .map(customerMapper::toResponse)
                                                                .toList())
                                .vehicleVisits(
                                                visits.stream()
                                                                .map(customerMapper::toHistoryResponse)
                                                                .toList())
                                .build();
        }

        @Override
        @Transactional
        public CustomerResponseDto updateCustomer(UUID customerId, CustomerUpdateDto updateDto) {
                Customer customer = customerRepository.findById(customerId)
                                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));

                // Partial update: only overwrite fields the client actually sent.
                if (updateDto.getCustomerName() != null && !updateDto.getCustomerName().isBlank()) {
                        customer.setCustomerName(updateDto.getCustomerName().trim());
                }

                if (updateDto.getCustomerPhone() != null && !updateDto.getCustomerPhone().isBlank()) {
                        String normalizedPhone = updateDto.getCustomerPhone().trim();
                        if (!normalizedPhone.equals(customer.getCustomerPhone())
                                        && customerRepository.existsByCustomerPhone(normalizedPhone)) {
                                throw new DuplicateResourceException("A customer already exists with phone " + normalizedPhone);
                        }
                        customer.setCustomerPhone(normalizedPhone);
                }

                return customerMapper.toResponse(customerRepository.save(customer));
        }

        @Override
        @Transactional(readOnly = true)
        public CustomerResponseDto getCustomerById(UUID customerId) {
                Customer customer = customerRepository.findById(customerId)
                                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));
                return customerMapper.toResponse(customer);
        }

        @Override
        @Transactional
        public void deleteCustomer(UUID customerId) {
                log.info("Deleting customer with ID: {}", customerId);
                Customer customer = customerRepository.findById(customerId)
                                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));
                customerRepository.delete(customer);
                log.info("Successfully deleted customer with ID: {}", customerId);
        }
}
