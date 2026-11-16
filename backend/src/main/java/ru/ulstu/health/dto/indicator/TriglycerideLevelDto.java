package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TriglycerideLevelDto {
    private String id;

    @NotNull(message = "Значение уровня триглицеридов обязательно")
    private Double value;

    private LocalDateTime createdAt;
}
