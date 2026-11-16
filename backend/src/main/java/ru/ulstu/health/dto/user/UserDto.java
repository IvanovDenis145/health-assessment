package ru.ulstu.health.dto.user;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserDto {
    private String id;
    private String fio;
    private String phoneNumber;
    private String email;
    private Integer age;
    private String gender;
    private LocalDateTime createdAt;
}