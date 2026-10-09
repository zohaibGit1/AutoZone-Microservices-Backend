package com.Vechile_Service.service.impl;

import com.Vechile_Service.dto.VehicleRequestDto;
import com.Vechile_Service.dto.VehicleResponseDto;
import com.Vechile_Service.entity.Customer;
import com.Vechile_Service.entity.Vehicle;
import com.Vechile_Service.exception.DuplicateResourceException;
import com.Vechile_Service.exception.ResourceNotFoundException;
import com.Vechile_Service.mapper.Mapper;
import com.Vechile_Service.repo.CustomerRepository;
import com.Vechile_Service.repo.VehicleRepository;
import com.Vechile_Service.service.VehicleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerRepository customerRepository;
    private final Mapper mapper;

    @Override
    @Transactional
    public VehicleResponseDto registerVehicle(VehicleRequestDto vehicleRequestDto) {
        log.info("Registering vehicle with number: {} for customer: {}", vehicleRequestDto.getVehicleNumber(), vehicleRequestDto.getCustomerId());
        Customer customerSearch = customerRepository.findById(vehicleRequestDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Customer Not Found with this id " + vehicleRequestDto.getCustomerId()));

        String normalizedVehicleNumber = vehicleRequestDto.getVehicleNumber().toUpperCase().trim();

        if (vehicleRepository.existsByVehicleNumberIgnoreCase(normalizedVehicleNumber)) {
            throw new DuplicateResourceException(
                    "A vehicle already exists with number " + normalizedVehicleNumber);
        }

        Vehicle vehicle = Vehicle.builder()
                .vehicleModel(vehicleRequestDto.getVehicleModel())
                .vehicleNumber(normalizedVehicleNumber)
                .vehicleType(vehicleRequestDto.getVehicleType())
                .vehicleName(vehicleRequestDto.getVehicleName())
                .customer(customerSearch)
                .build();
        Vehicle saved = vehicleRepository.save(vehicle);
        VehicleResponseDto response = mapper.toResponse(saved);
        log.info("Successfully registered vehicle with ID: {}", response.getVehicleId());
        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public VehicleResponseDto getVehicleById(UUID vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + vehicleId));
        return mapper.toResponse(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<VehicleResponseDto> getVehiclesByCustomerId(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + customerId));
        return customer.getVehicles().stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void deleteVehicle(UUID vehicleId) {
        log.info("Deleting vehicle with ID: {}", vehicleId);
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + vehicleId));
        vehicleRepository.delete(vehicle);
        log.info("Successfully deleted vehicle with ID: {}", vehicleId);
    }
}
