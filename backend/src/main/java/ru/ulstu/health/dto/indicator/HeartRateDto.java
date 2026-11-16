package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class HeartRateDto {
    private String id;

    @NotNull(message = "Значение ЧСС обязательно")
    private Integer value;

    private LocalDateTime createdAt;
}
