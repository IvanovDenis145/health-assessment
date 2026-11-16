package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StateOfHealthDto {
    private String id;

    @NotNull(message = "Значение оценки здоровья обязательно")
    private Double value;

    @Size(max = 20, message = "Категория не должна превышать 20 символов")
    private String name;

    private LocalDateTime createdAt;
}
