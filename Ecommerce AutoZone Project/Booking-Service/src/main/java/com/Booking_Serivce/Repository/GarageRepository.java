package com.Booking_Serivce.Repository;

import com.Booking_Serivce.Constant.EntityStatus;
import com.Booking_Serivce.Entity.Garage;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GarageRepository extends JpaRepository<Garage, UUID> {
    List<Garage> findByStatus(EntityStatus status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT g FROM Garage g WHERE g.garageId = :garageId")
    Optional<Garage> findByIdForUpdate(@Param("garageId") UUID garageId);
}
