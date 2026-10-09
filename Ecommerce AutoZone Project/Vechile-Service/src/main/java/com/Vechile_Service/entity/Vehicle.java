package com.Vechile_Service.entity;

import com.Vechile_Service.constant.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "vehicle_id", updatable = false, nullable = false)
    private UUID vehicleId;

    @Column(name = "vehicle_number", unique = true, nullable = false, length = 20)
    private String vehicleNumber;

    @Column(name = "vehicle_name", nullable = false, length = 50)
    private String vehicleName;

    @Column(name = "vehicle_model", nullable = false, length = 50)
    private String vehicleModel;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type", nullable = false, length = 20)
    private VehicleType vehicleType;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<VehicleVisit> vehicleVisits = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (vehicleNumber != null) {
            vehicleNumber = vehicleNumber.toUpperCase().trim();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
