package ru.ulstu.health.dto.indicator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class MentalDisorderDto {
    private String id;

    @NotBlank(message = "Название расстройства не может быть пустым")
    @Size(max = 50, message = "Название расстройства не должно превышать 50 символов")
    private String name;

    private LocalDateTime createdAt;
}
