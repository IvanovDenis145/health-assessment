package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ThicknessOfTheSkinFatFoldDto {
    private String id;

    @NotNull(message = "Значение толщины кожно-жировой складки обязательно")
    private Integer value;

    private LocalDateTime createdAt;
}
