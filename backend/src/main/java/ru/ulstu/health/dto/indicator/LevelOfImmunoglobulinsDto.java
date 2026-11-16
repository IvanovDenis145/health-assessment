package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LevelOfImmunoglobulinsDto {
    private String id;

    @NotNull(message = "Значение уровня иммуноглобулинов обязательно")
    private Double value;

    private LocalDateTime createdAt;
}
