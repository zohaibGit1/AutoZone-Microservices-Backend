package com.Vechile_Service.repo;

import com.Vechile_Service.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findByCustomerEmailIgnoreCase(String customerEmail);

    Optional<Customer> findByCustomerPhone(String customerPhone);

    boolean existsByCustomerEmailIgnoreCase(String customerEmail);

    boolean existsByCustomerPhone(String customerPhone);
}
