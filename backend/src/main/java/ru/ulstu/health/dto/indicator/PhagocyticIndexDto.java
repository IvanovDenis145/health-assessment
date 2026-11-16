package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PhagocyticIndexDto {
    private String id;

    @NotNull(message = "Значение фагоцитарного числа обязательно")
    private Double value;

    private LocalDateTime createdAt;
}
