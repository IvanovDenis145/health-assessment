package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class DeadliftStrengthDto {
    private String id;

    @NotNull(message = "Значение становой силы обязательно")
    private Integer value;

    private LocalDateTime createdAt;
}
