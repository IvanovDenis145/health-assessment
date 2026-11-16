package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SubjectiveWellBeingDto {
    private String id;

    @NotNull(message = "Оценка самочувствия обязательна")
    @Min(value = 1, message = "Оценка должна быть от 1 до 10")
    @Max(value = 10, message = "Оценка должна быть от 1 до 10")
    private Integer value;

    private LocalDateTime createdAt;
}
