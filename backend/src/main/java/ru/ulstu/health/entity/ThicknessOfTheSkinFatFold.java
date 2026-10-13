package ru.ulstu.health.entity;

import jakarta.persistence.*;
import lombok.*;

// --- Антропометрические ---

@Entity
@Table(name = "thickness_of_the_skin_fat_fold")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ThicknessOfTheSkinFatFold extends BaseEntity {
    @Column(name = "value", nullable = false)
    private Integer value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
