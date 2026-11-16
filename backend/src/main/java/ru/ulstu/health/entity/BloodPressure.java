package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Функциональное состояние ---

@Entity @Table(name = "blood_pressure")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class BloodPressure extends BaseEntity {
    @Column(name = "upper_pressure", nullable = false)
    private Integer upperPressure;

    @Column(name = "lower_pressure", nullable = false)
    private Integer lowerPressure;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
