package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HeightDto {
    private String id;

    @NotNull(message = "Значение роста обязательно")
    private Integer value;

    private LocalDateTime createdAt;
}
