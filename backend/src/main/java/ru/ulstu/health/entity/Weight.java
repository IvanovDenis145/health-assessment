package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Антропометрические ---

@Entity
@Table(name = "weight")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Weight extends BaseEntity {
    @Column(name = "value", nullable = false)
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
