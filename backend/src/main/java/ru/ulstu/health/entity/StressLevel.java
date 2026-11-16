package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Психическое здоровье ---

@Entity @Table(name = "stress_level")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StressLevel extends BaseEntity {
    @Column(name = "value", nullable = false)
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
