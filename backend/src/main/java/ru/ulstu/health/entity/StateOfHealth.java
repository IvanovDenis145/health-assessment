package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Оценка состояния здоровья ---

@Entity @Table(name = "state_of_health")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class StateOfHealth extends BaseEntity {
    @Column(name = "value", nullable = false)
    private Double value;

    @Column(name = "name", length = 20)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
