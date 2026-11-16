package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Лабораторные и биохимические маркеры ---

@Entity @Table(name = "glucose_level")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class GlucoseLevel extends BaseEntity {
    @Column(name = "value", nullable = false)
    private Double value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
