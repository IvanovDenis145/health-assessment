package ru.ulstu.health.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserUpdateDto {
    @NotBlank(message = "ФИО не может быть пустым")
    private String fio;

    private String phoneNumber;

    @NotBlank(message = "Email обязателен")
    @Email(message = "Некорректный формат Email")
    private String email;

    private Integer age;
    private String gender;
}
