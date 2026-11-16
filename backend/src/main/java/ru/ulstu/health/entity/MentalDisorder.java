package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Психическое здоровье ---

@Entity @Table(name = "mental_disorder")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MentalDisorder extends BaseEntity {
    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
