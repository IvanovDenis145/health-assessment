package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Функциональное состояние ---

@Entity @Table(name = "heart_rate")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class HeartRate extends BaseEntity {
    @Column(name = "value", nullable = false)
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
