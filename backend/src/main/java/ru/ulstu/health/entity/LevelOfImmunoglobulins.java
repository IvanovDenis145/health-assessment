package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Иммунный статус ---

@Entity @Table(name = "level_of_immunoglobulins")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class LevelOfImmunoglobulins extends BaseEntity {
    @Column(name = "value", nullable = false)
    private Double value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
