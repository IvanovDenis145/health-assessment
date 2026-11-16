package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class BloodPressureDto {
    private String id;

    @NotNull(message = "Верхнее давление обязательно")
    private Integer upperPressure;

    @NotNull(message = "Нижнее давление обязательно")
    private Integer lowerPressure;

    private LocalDateTime createdAt;
}
