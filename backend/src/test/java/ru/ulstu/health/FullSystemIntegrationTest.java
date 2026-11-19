package ru.ulstu.health;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.ulstu.health.dto.indicator.HeightDto;
import ru.ulstu.health.dto.user.JwtAuthResponseDto;
import ru.ulstu.health.dto.user.LoginRequestDto;
import ru.ulstu.health.dto.user.UserCreateDto;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FullSystemIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    // Вспомогательный метод для регистрации и получения токена
    private String registerAndGetToken(String email, String password) {
        // 1. Регистрация
        UserCreateDto createDto = new UserCreateDto();
        createDto.setFio("Тестовый Пользователь");
        createDto.setEmail(email);
        createDto.setPassword(password);
        createDto.setAge(30);
        createDto.setGender("male");

        restTemplate.postForEntity("/api/auth/register", createDto, String.class);

        // 2. Логин
        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);

        ResponseEntity<JwtAuthResponseDto> loginResponse = restTemplate.postForEntity(
                "/api/auth/login", loginDto, JwtAuthResponseDto.class);

        return loginResponse.getBody().getAccessToken();
    }

    // Вспомогательный метод для создания заголовков с токеном
    private HttpHeaders getHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Test
    @DisplayName("1. Обычный пользователь не имеет доступа к списку всех пользователей (403)")
    void userCannotAccessAdminEndpoint() {
        String token = registerAndGetToken("user1@test.com", "password123");

        HttpEntity<Void> request = new HttpEntity<>(getHeaders(token));
        ResponseEntity<String> response = restTemplate.exchange(
                "/api/users", HttpMethod.GET, request, String.class);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    @DisplayName("2. Администратор может получить список всех пользователей и удалить юзера (200, 204)")
    void adminCanManageUsers() {
        String adminEmail = "admin@test.com";
        String adminToken = registerAndGetToken(adminEmail, "password123");

        // Делаем пользователя админом через репозиторий (имитация миграции V11)
        User admin = userRepository.findByEmail(adminEmail).orElseThrow();
        admin.setRole("ROLE_ADMIN");
        userRepository.save(admin);

        // Создаем жертву для удаления
        String victimEmail = "victim@test.com";
        registerAndGetToken(victimEmail, "password123");
        User victim = userRepository.findByEmail(victimEmail).orElseThrow();

        HttpHeaders headers = getHeaders(adminToken);

        // Проверка получения списка
        ResponseEntity<List> listResponse = restTemplate.exchange(
                "/api/users", HttpMethod.GET, new HttpEntity<>(headers), List.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertTrue(listResponse.getBody().size() >= 2);

        // Проверка удаления
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/users/" + victim.getId(), HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertFalse(userRepository.existsByEmail(victimEmail));
    }

    @Test
    @DisplayName("3. Полный CRUD для показателя Рост (создание, чтение, обновление, удаление)")
    void heightCrudLifecycle() {
        String email = "height_test@test.com";
        String token = registerAndGetToken(email, "password123");
        User user = userRepository.findByEmail(email).orElseThrow();
        String userId = user.getId();
        HttpHeaders headers = getHeaders(token);

        // 1. CREATE
        HeightDto createDto = new HeightDto();
        createDto.setValue(180);
        ResponseEntity<HeightDto> createResponse = restTemplate.exchange(
                "/api/users/" + userId + "/heights", HttpMethod.POST,
                new HttpEntity<>(createDto, headers), HeightDto.class);
        assertEquals(HttpStatus.CREATED, createResponse.getStatusCode());
        String heightId = createResponse.getBody().getId();

        // 2. READ (list)
        ResponseEntity<List> listResponse = restTemplate.exchange(
                "/api/users/" + userId + "/heights", HttpMethod.GET,
                new HttpEntity<>(headers), List.class);
        assertEquals(HttpStatus.OK, listResponse.getStatusCode());
        assertEquals(1, listResponse.getBody().size());

        // 3. UPDATE
        HeightDto updateDto = new HeightDto();
        updateDto.setValue(185);
        ResponseEntity<HeightDto> updateResponse = restTemplate.exchange(
                "/api/users/" + userId + "/heights/" + heightId,
                HttpMethod.PUT, new HttpEntity<>(updateDto, headers), HeightDto.class);
        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals(185, updateResponse.getBody().getValue());

        // 4. DELETE
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/users/" + userId + "/heights/" + heightId,
                HttpMethod.DELETE, new HttpEntity<>(headers), Void.class);
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
    }

    @Test
    @DisplayName("4. Алгоритм оценки здоровья возвращает результат при наличии данных")
    void healthAssessmentAlgorithmWorks() {
        String email = "assessment_test@test.com";
        String token = registerAndGetToken(email, "password123");
        User user = userRepository.findByEmail(email).orElseThrow();
        String userId = user.getId();
        HttpHeaders headers = getHeaders(token);

        // Добавляем обязательные показатели (Рост, Вес и т.д. - упрощенно добавляем 3 для теста)
        restTemplate.exchange("/api/users/" + userId + "/heights",
                HttpMethod.POST, new HttpEntity<>(Map.of("value", 170), headers), String.class);
        restTemplate.exchange("/api/users/" + userId + "/weights",
                HttpMethod.POST, new HttpEntity<>(Map.of("value", 70), headers), String.class);
        restTemplate.exchange("/api/users/" + userId + "/heart-rates",
                HttpMethod.POST, new HttpEntity<>(Map.of("value", 72), headers), String.class);

        // Запускаем оценку
        ResponseEntity<Map> assessResponse = restTemplate.exchange(
                "/api/users/" + userId + "/assessment", HttpMethod.POST,
                new HttpEntity<>(headers), Map.class);

        assertEquals(HttpStatus.OK, assessResponse.getStatusCode());
        assertNotNull(assessResponse.getBody().get("value"));
        assertNotNull(assessResponse.getBody().get("name"));

        double score = (Double) assessResponse.getBody().get("value");
        assertTrue(score >= 0.0 && score <= 10.0);
    }

    @Test
    @DisplayName("5. История оценок сохраняется и возвращается корректно")
    void assessmentHistoryIsSaved() {
        String email = "history_test@test.com";
        String token = registerAndGetToken(email, "password123");
        User user = userRepository.findByEmail(email).orElseThrow();
        String userId = user.getId();
        HttpHeaders headers = getHeaders(token);

        // Создаем оценку
        restTemplate.exchange("/api/users/" + userId + "/assessment",
                HttpMethod.POST, new HttpEntity<>(headers), String.class);
        restTemplate.exchange("/api/users/" + userId + "/assessment",
                HttpMethod.POST, new HttpEntity<>(headers), String.class);

        // Запрашиваем историю
        ResponseEntity<List> historyResponse = restTemplate.exchange(
                "/api/users/" + userId + "/assessment", HttpMethod.GET,
                new HttpEntity<>(headers), List.class);

        assertEquals(HttpStatus.OK, historyResponse.getStatusCode());
        assertEquals(2, historyResponse.getBody().size(), "Должно быть 2 записи в истории");
    }
}
