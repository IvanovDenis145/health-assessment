package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GlucoseLevelDto {
    private String id;

    @NotNull(message = "Значение уровня глюкозы обязательно")
    private Double value;

    private LocalDateTime createdAt;
}
