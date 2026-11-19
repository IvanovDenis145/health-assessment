package ru.ulstu.health.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ulstu.health.dto.user.JwtAuthResponseDto;
import ru.ulstu.health.dto.user.LoginRequestDto;
import ru.ulstu.health.dto.user.UserCreateDto;
import ru.ulstu.health.dto.user.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldRegisterLoginAndRetrieveProfile() {
        // 1. Подготовка данных для регистрации
        UserCreateDto createDto = new UserCreateDto();
        createDto.setFio("Интеграционный Тест");
        createDto.setEmail("integration@test.com");
        createDto.setPassword("password123");
        createDto.setAge(25);
        createDto.setGender("male");

        // 2. Регистрация (открытый эндпоинт)
        ResponseEntity<UserDto> createResponse = restTemplate.postForEntity(
                "/api/auth/register",
                createDto,
                UserDto.class
        );

        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        assertNotNull(createResponse.getBody());
        String userId = createResponse.getBody().getId();

        // 3. Логин для получения JWT токена
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail("integration@test.com");
        loginDto.setPassword("password123");

        ResponseEntity<JwtAuthResponseDto> loginResponse = restTemplate.postForEntity(
                "/api/auth/login",
                loginDto,
                JwtAuthResponseDto.class
        );

        assertEquals(HttpStatus.OK, loginResponse.getStatusCode());
        assertNotNull(loginResponse.getBody());
        String token = loginResponse.getBody().getAccessToken();

        // 4. Запрос защищенного профиля с использованием токена
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Устанавливаем заголовок Authorization: Bearer <token>
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<UserDto> getResponse = restTemplate.exchange(
                "/api/users/" + userId,
                HttpMethod.GET,
                requestEntity,
                UserDto.class
        );

        // 5. Проверка результата
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());
        assertNotNull(getResponse.getBody());
        assertEquals("Интеграционный Тест", getResponse.getBody().getFio());
        assertEquals("integration@test.com", getResponse.getBody().getEmail());
    }
}
