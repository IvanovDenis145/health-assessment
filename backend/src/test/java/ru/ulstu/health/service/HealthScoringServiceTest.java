package ru.ulstu.health.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.ulstu.health.config.HealthNormsProperties;
import ru.ulstu.health.entity.*;
import ru.ulstu.health.repository.StateOfHealthRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) // Отключает строгую проверку неиспользуемых моков
class HealthScoringServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private StateOfHealthRepository stateOfHealthRepository;

    @Mock
    private HealthNormsProperties norms;

    @InjectMocks
    private HealthScoringService scoringService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId("user-1");
        testUser.setAge(30);
        testUser.setGender("male");

        testUser.setHeights(new ArrayList<>());
        testUser.setWeights(new ArrayList<>());
        testUser.setHeartRates(new ArrayList<>());
        testUser.setBloodPressures(new ArrayList<>());
        testUser.setStressLevels(new ArrayList<>());
        testUser.setMentalDisorders(new ArrayList<>());

        // Мокаем сохранение в БД
        when(stateOfHealthRepository.save(any(StateOfHealth.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    private void mockBasicNorms() {
        HealthNormsProperties.StandardNorm hrNorm = new HealthNormsProperties.StandardNorm();
        hrNorm.setIdeal(75); hrNorm.setMin(60); hrNorm.setMax(90);
        when(norms.getHeartRateNorm(anyInt())).thenReturn(hrNorm);

        HealthNormsProperties.StandardNorm bmiNorm = new HealthNormsProperties.StandardNorm();
        bmiNorm.setIdeal(22); bmiNorm.setMin(18.5); bmiNorm.setMax(25.0);
        when(norms.getBmiNorm(anyInt())).thenReturn(bmiNorm);
    }

    @Test
    @DisplayName("Пользователь не найден - должно выбрасываться исключение")
    void evaluateUserHealth_UserNotFound_ThrowsException() {
        when(userRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> scoringService.evaluateUserHealth("invalid"));
    }

    @Test
    @DisplayName("Идеальные показатели - итоговый балл должен быть 10.0 (Здоровый)")
    void evaluateUserHealth_PerfectHealth_ReturnsHighScore() {
        mockBasicNorms();

        // ИЗМЕНЕНО: Рост 200, вес 88, чтобы ИМТ был ровно 22.0 (идеал)
        Height height = new Height(); height.setValue(200); testUser.getHeights().add(height);
        Weight weight = new Weight(); weight.setValue(88); testUser.getWeights().add(weight);

        // Пульс 75 (идеал)
        HeartRate hr = new HeartRate(); hr.setValue(75); testUser.getHeartRates().add(hr);

        when(userRepository.findById("user-1")).thenReturn(Optional.of(testUser));

        StateOfHealth result = scoringService.evaluateUserHealth("user-1");

        assertNotNull(result);
        assertEquals(10.0, result.getValue());
        assertEquals("Здоровый", result.getName());
    }

    @Test
    @DisplayName("Плохие показатели - балл должен быть низким (Больной)")
    void evaluateUserHealth_BadHealth_ReturnsLowScore() {
        mockBasicNorms();

        HeartRate hr = new HeartRate(); hr.setValue(140); testUser.getHeartRates().add(hr);

        MentalDisorder disorder = new MentalDisorder(); disorder.setName("Депрессия");
        testUser.getMentalDisorders().add(disorder);

        when(userRepository.findById("user-1")).thenReturn(Optional.of(testUser));

        StateOfHealth result = scoringService.evaluateUserHealth("user-1");

        assertNotNull(result);
        assertTrue(result.getValue() < 5.0, "Балл должен быть ниже 5.0");
        assertEquals("Больной", result.getName());
    }

    @Test
    @DisplayName("Нет данных - алгоритм не падает, выдает средний балл (5.0)")
    void evaluateUserHealth_EmptyData_ReturnsDefaultScore() {
        // Пользователь без единого показателя
        when(userRepository.findById("user-1")).thenReturn(Optional.of(testUser));

        StateOfHealth result = scoringService.evaluateUserHealth("user-1");

        assertNotNull(result);
        // Так как данных нет вообще (включая психику), алгоритм выдает 5.0
        assertEquals(5.0, result.getValue());
        assertEquals("Среднее состояние", result.getName());
    }

    @Test
    @DisplayName("Проверка расчета давления (верхнее и нижнее усредняются)")
    void evaluateUserHealth_BloodPressureAverage() {
        HealthNormsProperties.BloodPressureNorm bpNorm = new HealthNormsProperties.BloodPressureNorm();
        bpNorm.setSysIdeal(120); bpNorm.setSysMin(110); bpNorm.setSysMax(130);
        bpNorm.setDiaIdeal(80); bpNorm.setDiaMin(70); bpNorm.setDiaMax(85);
        when(norms.getBloodPressureNorm(anyInt())).thenReturn(bpNorm);

        when(userRepository.findById("user-1")).thenReturn(Optional.of(testUser));

        BloodPressure bp = new BloodPressure();
        bp.setUpperPressure(120);
        bp.setLowerPressure(80);
        testUser.getBloodPressures().add(bp);

        StateOfHealth result = scoringService.evaluateUserHealth("user-1");

        // Давление идеальное, должно дать 10.0
        assertEquals(10.0, result.getValue());
    }
}
