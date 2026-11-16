package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Лабораторные и биохимические маркеры ---

@Entity @Table(name = "cholesterol_level")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class CholesterolLevel extends BaseEntity {
    @Column(name = "value", nullable = false)
    private Double value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
