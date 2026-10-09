package com.Booking_Serivce.Repository;

import com.Booking_Serivce.Constant.EntityStatus;
import com.Booking_Serivce.Entity.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<ServiceType, UUID> {
    List<ServiceType> findByStatus(EntityStatus status);
}
