package com.Vechile_Service.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "vehicle_visits")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "visit_id", updatable = false, nullable = false)
    private UUID visitId;

    @Column(name = "visit_date", nullable = false)
    private LocalDate visitDate;

    @Column(name = "current_km", nullable = false)
    private int currentKm;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    // Set later once Booking Service is wired up (staff check-in triggers this visit's creation
    // from a specific booking). Nullable for now since visits can also be created without a
    // prior booking (walk-in customers).
    @Column(name = "booking_id")
    private UUID bookingId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @OneToMany(mappedBy = "vehicleVisit", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Complaint> complaints = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        visitDate = LocalDate.now();
        checkInTime = LocalDateTime.now();
    }

}
