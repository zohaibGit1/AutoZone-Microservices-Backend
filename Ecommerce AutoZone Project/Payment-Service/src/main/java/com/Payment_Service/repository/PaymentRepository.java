package com.Payment_Service.repository;

import com.Payment_Service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByBookingId(UUID bookingId);

    boolean existsByBookingId(UUID bookingId);

    Optional<Payment> findByTransactionId(String transactionId);

    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
}
