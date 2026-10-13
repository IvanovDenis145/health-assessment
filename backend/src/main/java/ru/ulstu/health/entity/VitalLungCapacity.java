package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Физиометрические ---

@Entity @Table(name = "vital_lung_capacity")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class VitalLungCapacity extends BaseEntity {
    @Column(name = "value", nullable = false)
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
