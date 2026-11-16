package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StressLevelDto {
    private String id;

    @NotNull(message = "Значение уровня стресса обязательно")
    @Min(value = 1, message = "Уровень стресса должен быть от 1 до 10")
    @Max(value = 10, message = "Уровень стресса должен быть от 1 до 10")
    private Integer value;

    private LocalDateTime createdAt;
}
