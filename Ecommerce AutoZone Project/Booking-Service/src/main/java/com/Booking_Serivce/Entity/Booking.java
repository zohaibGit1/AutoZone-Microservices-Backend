package com.Booking_Serivce.Entity;

// entity/Booking.java
import com.Booking_Serivce.Constant.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "BOOKING")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id", updatable = false, nullable = false)
    private UUID bookingId;

    // No @ManyToOne here — Customer/Vehicle live in a different microservice/DB.
    // Store the raw ID only; validate via a Feign/REST call to Vehicle Service, not a JPA join.
    @Column(name = "customer_id", nullable = false)
    private UUID customerId;

    @Column(name = "vehicle_id", nullable = false)
    private UUID vehicleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "garage_id", nullable = false, foreignKey = @ForeignKey(name = "garage_id_fk"))
    private Garage garage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", nullable = false, foreignKey = @ForeignKey(name = "service_id_fk"))
    private ServiceType serviceType;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "booking_status", nullable = false, length = 30)
    private BookingStatus bookingStatus;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (bookingStatus == null) bookingStatus = BookingStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}