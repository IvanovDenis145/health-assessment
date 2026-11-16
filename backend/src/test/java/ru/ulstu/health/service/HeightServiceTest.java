package ru.ulstu.health.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.ulstu.health.entity.Height;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.HeightRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HeightServiceTest {

    @Mock
    private HeightRepository heightRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HeightService heightService;

    private User testUser;
    private Height testHeight;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user-1");

        testHeight = new Height();
        testHeight.setId("height-1");
        testHeight.setValue(180);
        testHeight.setUser(testUser);
    }

    @Test
    @DisplayName("Создание роста - успех")
    void create_Success() {
        when(userRepository.findById("user-1")).thenReturn(Optional.of(testUser));
        when(heightRepository.save(any(Height.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Height created = heightService.create("user-1", testHeight);

        assertNotNull(created);
        assertEquals(180, created.getValue());
        assertEquals("user-1", created.getUser().getId());
    }

    @Test
    @DisplayName("Создание роста - пользователь не найден (ошибка)")
    void create_UserNotFound_ThrowsException() {
        when(userRepository.findById("invalid-user")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> heightService.create("invalid-user", testHeight));
    }

    @Test
    @DisplayName("Поиск по ID - успех")
    void findById_Success() {
        when(heightRepository.findById("height-1")).thenReturn(Optional.of(testHeight));

        Height found = heightService.findById("height-1");

        assertNotNull(found);
        assertEquals(180, found.getValue());
    }

    @Test
    @DisplayName("Поиск по ID - не найден (ошибка)")
    void findById_NotFound_ThrowsException() {
        when(heightRepository.findById("invalid-id")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> heightService.findById("invalid-id"));
    }

    @Test
    @DisplayName("Поиск всех по пользователю - успех")
    void findAllByUser_Success() {
        when(heightRepository.findAllByUserId("user-1")).thenReturn(List.of(testHeight));

        List<Height> list = heightService.findAllByUser("user-1");

        assertEquals(1, list.size());
        assertEquals(180, list.get(0).getValue());
    }

    @Test
    @DisplayName("Обновление - успех")
    void update_Success() {
        Height updatedInfo = new Height();
        updatedInfo.setValue(185);

        when(heightRepository.findById("height-1")).thenReturn(Optional.of(testHeight));
        when(heightRepository.save(any(Height.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Height result = heightService.update("height-1", updatedInfo);

        assertEquals(185, result.getValue());
    }

    @Test
    @DisplayName("Удаление - успех")
    void delete_Success() {
        when(heightRepository.existsById("height-1")).thenReturn(true);

        heightService.delete("height-1");

        verify(heightRepository, times(1)).deleteById("height-1");
    }

    @Test
    @DisplayName("Удаление - не найден (ошибка)")
    void delete_NotFound_ThrowsException() {
        when(heightRepository.existsById("invalid-id")).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> heightService.delete("invalid-id"));
        verify(heightRepository, never()).deleteById(anyString());
    }
}