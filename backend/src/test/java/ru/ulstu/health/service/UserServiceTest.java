package ru.ulstu.health.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user-1");
        testUser.setFio("Иванов Иван");
        testUser.setEmail("ivanov@example.com");
        testUser.setPassword("secret123");
    }

    @Test
    @DisplayName("Создание пользователя - успех (пароль хэшируется)")
    void createUser_Success() {
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed_password");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = userService.create(testUser);

        assertNotNull(created);
        assertEquals("hashed_password", created.getPassword(), "Пароль должен быть зашифрован");
        verify(userRepository, times(1)).save(testUser);
    }

    @Test
    @DisplayName("Создание пользователя - email уже занят (ошибка)")
    void createUser_EmailExists_ThrowsException() {
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> userService.create(testUser));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Поиск по ID - успех")
    void findById_Success() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(testUser));

        User found = userService.findById("user-1");

        assertNotNull(found);
        assertEquals("user-1", found.getId());
    }

    @Test
    @DisplayName("Поиск по ID - пользователь не найден (ошибка)")
    void findById_NotFound_ThrowsException() {
        when(userRepository.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById("invalid-id"));
    }

    @Test
    @DisplayName("Обновление пользователя - успех (пароль меняется и хэшируется)")
    void updateUser_Success() {
        User updatedInfo = new User();
        updatedInfo.setFio("Петров Петр");
        updatedInfo.setEmail("petrov@example.com");
        updatedInfo.setPassword("new_secret");

        when(userRepository.findById("user-1")).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("new_secret")).thenReturn("new_hashed");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.update("user-1", updatedInfo);

        assertEquals("Петров Петр", result.getFio());
        assertEquals("petrov@example.com", result.getEmail());
        assertEquals("new_hashed", result.getPassword());
    }

    @Test
    @DisplayName("Обновление пользователя - пароль не передан (хэш не меняется)")
    void updateUser_NoPassword_KeepsOldPassword() {
        User updatedInfo = new User();
        updatedInfo.setFio("Петров Петр");
        updatedInfo.setEmail("petrov@example.com");
        updatedInfo.setPassword(null); // Пароль не передан

        when(userRepository.findById("user-1")).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.update("user-1", updatedInfo);

        assertEquals("secret123", result.getPassword(), "Старый пароль должен сохраниться");
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    @DisplayName("Удаление пользователя - успех")
    void deleteUser_Success() {
        when(userRepository.existsById("user-1")).thenReturn(true);

        userService.delete("user-1");

        verify(userRepository, times(1)).deleteById("user-1");
    }

    @Test
    @DisplayName("Удаление пользователя - не найден (ошибка)")
    void deleteUser_NotFound_ThrowsException() {
        when(userRepository.existsById("invalid-id")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> userService.delete("invalid-id"));
        verify(userRepository, never()).deleteById(anyString());
    }
}
