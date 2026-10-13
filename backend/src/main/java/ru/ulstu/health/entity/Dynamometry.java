package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Физиометрические ---

@Entity @Table(name = "dynamometry")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Dynamometry extends BaseEntity {
    @Column(name = "value", nullable = false)
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
