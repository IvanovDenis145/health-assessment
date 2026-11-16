package ru.ulstu.health.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDto {
    @NotBlank(message = "ФИО не может быть пустым")
    @Size(max = 50, message = "ФИО не должно превышать 50 символов")
    private String fio;

    @Size(max = 11, message = "Телефон не должен превышать 11 символов")
    private String phoneNumber;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат Email")
    private String email;

    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен быть не менее 6 символов")
    private String password;

    private Integer age;
    private String gender;
}
