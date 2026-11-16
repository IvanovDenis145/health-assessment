package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Субъективное самочувствие ---

@Entity @Table(name = "subjective_well_being")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SubjectiveWellBeing extends BaseEntity {
    @Column(name = "value", nullable = false)
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
