package com.Booking_Serivce.Repository;

import com.Booking_Serivce.Constant.BookingStatus;
import com.Booking_Serivce.Entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BookingRepository extends JpaRepository<Booking, UUID> {

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.garage.garageId = :garageId " +
            "AND b.bookingStatus IN :statuses " +
            "AND b.startTime < :endTime AND b.endTime > :startTime")
    long countOverlappingBookingsForGarage(
            @Param("garageId") UUID garageId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("statuses") Collection<BookingStatus> statuses
    );

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.vehicleId = :vehicleId " +
            "AND b.bookingStatus IN :statuses " +
            "AND b.startTime < :endTime AND b.endTime > :startTime")
    long countOverlappingBookingsForVehicle(
            @Param("vehicleId") UUID vehicleId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("statuses") Collection<BookingStatus> statuses
    );


    @Query("SELECT b FROM Booking b WHERE b.garage.garageId = :garageId " +
            "AND b.bookingStatus IN :statuses " +
            "AND b.startTime < :dayEnd AND b.endTime > :dayStart")
    List<Booking> findActiveBookingsForGarageOnDay(
            @Param("garageId") UUID garageId,
            @Param("dayStart") LocalDateTime dayStart,
            @Param("dayEnd") LocalDateTime dayEnd,
            @Param("statuses") Collection<BookingStatus> statuses
    );

    List<Booking> findByCustomerId(UUID customerId);

    @Query("SELECT b FROM Booking b WHERE b.garage.garageId = :garageId")
    List<Booking> findByGarageId(@Param("garageId") UUID garageId);

    List<Booking> findByVehicleId(UUID vehicleId);
}

