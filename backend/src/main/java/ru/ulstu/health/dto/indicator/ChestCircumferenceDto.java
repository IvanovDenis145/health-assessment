package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ChestCircumferenceDto {
    private String id;

    @NotNull(message = "Значение окружности грудной клетки обязательно")
    private Integer value;

    private LocalDateTime createdAt;
}
