package ru.ulstu.health.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDto {
    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат Email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    private String password;
}
