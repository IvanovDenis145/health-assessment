package ru.ulstu.health.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.config.HealthNormsProperties;
import ru.ulstu.health.entity.*;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.UserRepository;
import ru.ulstu.health.repository.StateOfHealthRepository;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class HealthScoringService {

    private final UserRepository userRepository;
    private final StateOfHealthRepository stateOfHealthRepository;
    private final HealthNormsProperties norms;
    private final ObjectMapper objectMapper;

    @Transactional
    public StateOfHealth evaluateUserHealth(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));

        int age = user.getAge() != null ? user.getAge() : 30;
        String gender = user.getGender() != null ? user.getGender() : "male";

        Double weight = getLatestWeight(user);
        Double height = getLatestHeight(user);

        List<Double> scores = new ArrayList<>();
        Map<String, Double> categoryScores = new LinkedHashMap<>();

        Double bmiScore = evaluateBmi(height, weight, age);
        Double chestScore = evaluateChestCircumference(user, age, gender);
        Double skinFoldScore = evaluateSkinFold(user, age);

        List<Double> anthroScores = new ArrayList<>();
        if (bmiScore != null) {
            anthroScores.add(bmiScore);
        }
        if (chestScore != null) {
            anthroScores.add(chestScore);
        }
        if (skinFoldScore != null) {
            anthroScores.add(skinFoldScore);
        }

        Double anthroAvg = average(anthroScores);
        categoryScores.put("Антропометрия", anthroAvg);
        if (anthroAvg != null) {
            scores.add(anthroAvg);
        }

        Double vlcScore = evaluateVitalLungCapacity(user, age);
        Double dynamometryScore = evaluateDynamometry(user, age, gender);
        Double deadliftScore = evaluateDeadliftStrength(user, age, weight);

        List<Double> physioScores = new ArrayList<>();
        if (vlcScore != null) {
            physioScores.add(vlcScore);
        }
        if (dynamometryScore != null) {
            physioScores.add(dynamometryScore);
        }
        if (deadliftScore != null) {
            physioScores.add(deadliftScore);
        }

        Double physioAvg = average(physioScores);
        categoryScores.put("Физиометрия", physioAvg);
        if (physioAvg != null) {
            scores.add(physioAvg);
        }

        Double hrScore = evaluateHeartRate(user, age);
        Double bpScore = evaluateBloodPressure(user, age);
        Double rrScore = evaluateRespiratoryRate(user, age);

        List<Double> funcScores = new ArrayList<>();
        if (hrScore != null) {
            funcScores.add(hrScore);
        }
        if (bpScore != null) {
            funcScores.add(bpScore);
        }
        if (rrScore != null) {
            funcScores.add(rrScore);
        }

        Double funcAvg = average(funcScores);
        categoryScores.put("Функциональное состояние", funcAvg);
        if (funcAvg != null) {
            scores.add(funcAvg);
        }

        Double glucoseScore = evaluateGlucose(user, age);
        Double cholScore = evaluateCholesterol(user, age);
        Double trigScore = evaluateTriglyceride(user, age);

        List<Double> labScores = new ArrayList<>();
        if (glucoseScore != null) {
            labScores.add(glucoseScore);
        }
        if (cholScore != null) {
            labScores.add(cholScore);
        }
        if (trigScore != null) {
            labScores.add(trigScore);
        }

        Double labAvg = average(labScores);
        categoryScores.put("Лабораторные маркеры", labAvg);
        if (labAvg != null) {
            scores.add(labAvg);
        }

        Double iggScore = evaluateImmunoglobulinG(user, age);
        Double phagScore = evaluatePhagocyticIndex(user, age);

        List<Double> immuneScores = new ArrayList<>();
        if (iggScore != null) {
            immuneScores.add(iggScore);
        }
        if (phagScore != null) {
            immuneScores.add(phagScore);
        }

        Double immuneAvg = average(immuneScores);
        categoryScores.put("Иммунный статус", immuneAvg);
        if (immuneAvg != null) {
            scores.add(immuneAvg);
        }

        Double stressScore = evaluateStress(user, age);
        Double disorderScore = evaluateMentalDisorders(user);

        List<Double> mentalScores = new ArrayList<>();
        if (stressScore != null) {
            mentalScores.add(stressScore);
        }
        if (disorderScore != null) {
            mentalScores.add(disorderScore);
        }

        Double mentalAvg = average(mentalScores);
        categoryScores.put("Психическое здоровье", mentalAvg);
        if (mentalAvg != null) {
            scores.add(mentalAvg);
        }

        Double wellBeingScore = evaluateWellBeing(user, age);
        categoryScores.put("Субъективное самочувствие", wellBeingScore);
        if (wellBeingScore != null) {
            scores.add(wellBeingScore);
        }

        double totalScore = scores.isEmpty() ? 5.0 : scores.stream().mapToDouble
                (Double::doubleValue).average().orElse(5.0);
        totalScore = Math.round(totalScore * 10.0) / 10.0;

        // --- Формирование снимка ВСЕХ показателей (для истории) ---
        Map<String, Object> detailsMap = new LinkedHashMap<>();

        if (height != null) {
            detailsMap.put("Рост", height + " см");
        }
        if (weight != null) {
            detailsMap.put("Вес", weight + " кг");
        }
        if (chestScore != null) {
            detailsMap.put("Окружность груди", getLatestChest(user) + " см");
        }
        if (skinFoldScore != null) {
            detailsMap.put("Кожно-жировая складка", getLatestSkinFold(user) + " мм");
        }

        if (vlcScore != null) {
            detailsMap.put("ЖЕЛ", getLatestVlc(user) + " мл");
        }
        if (dynamometryScore != null) {
            detailsMap.put("Динамометрия", getLatestDynamometry(user) + " кг");
        }
        if (deadliftScore != null) {
            detailsMap.put("Становая сила", getLatestDeadlift(user) + " кг");
        }

        if (hrScore != null) {
            detailsMap.put("ЧСС", getLatestHr(user) + " уд/мин");
        }
        if (bpScore == null) {
        } else {
            BloodPressure bp = user.getBloodPressures().stream()
                    .max(Comparator.comparing(BloodPressure::getCreatedAt)).get();
            detailsMap.put("Давление", bp.getUpperPressure()
                    + "/" + bp.getLowerPressure() + " мм рт.ст.");
        }
        if (rrScore != null) {
            detailsMap.put("Частота дыхания", getLatestRr(user) + " вдохов/мин");
        }

        if (glucoseScore != null) {
            detailsMap.put("Глюкоза", getLatestGlucose(user) + " ммоль/л");
        }
        if (cholScore != null) {
            detailsMap.put("Холестерин", getLatestCholesterol(user) + " ммоль/л");
        }
        if (trigScore != null) {
            detailsMap.put("Триглицериды", getLatestTriglyceride(user) + " ммоль/л");
        }

        if (iggScore != null) {
            detailsMap.put("Иммуноглобулин G", getLatestIgg(user) + " г/л");
        }
        if (phagScore != null) {
            detailsMap.put("Фагоцитарный индекс", getLatestPhagocytic(user) + " у.е.");
        }

        if (stressScore != null) {
            detailsMap.put("Уровень стресса", getLatestStress(user) + " / 10");
        }
        if (user.getMentalDisorders() != null && !user.getMentalDisorders().isEmpty()) {
            detailsMap.put("Расстройства", user.getMentalDisorders().get(0).getName());
        } else {
            detailsMap.put("Расстройства", "Нет");
        }
        if (wellBeingScore != null) {
            detailsMap.put("Самочувствие", getLatestWellBeing(user) + " / 10");
        }

        Map<String, Object> chartData = new LinkedHashMap<>();
        categoryScores.forEach((catName, score) -> {
            if (score != null) {
                chartData.put(catName, score);
            }
        });
        detailsMap.put("_chartData", chartData);

        String detailsJson = "{}";
        try {
            detailsJson = objectMapper.writeValueAsString(detailsMap);
        } catch (Exception e) {
            log.error("Ошибка при сериализации деталей оценки", e);
        }

        StateOfHealth result = new StateOfHealth();
        result.setUser(user);
        result.setValue(totalScore);
        result.setName(interpretScore(totalScore));
        result.setDetails(detailsJson);

        log.info("Оценка здоровья пользователя {} (возраст {}): итог={}, категория={}",
                userId, age, totalScore, result.getName());

        return stateOfHealthRepository.save(result);
    }

    public List<StateOfHealth> getAssessmentHistory(String userId) {
        return stateOfHealthRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    // --- Вспомогательные методы для получения последних значений ---
    private Double getLatestWeight(User user) {
        return user.getWeights() == null || user.getWeights().isEmpty() ? null :
            user.getWeights().stream().max(Comparator.comparing(Weight::getCreatedAt))
            .get().getValue().doubleValue();
    }

    private Double getLatestHeight(User user) {
        return user.getHeights() == null || user.getHeights().isEmpty() ? null
            : user.getHeights().stream().max(Comparator.comparing(Height::getCreatedAt))
              .get().getValue().doubleValue();
    }

    private Double getLatestChest(User user) {
        return user.getChestCircumferences().stream().max(Comparator
            .comparing(ChestCircumference::getCreatedAt)).get()
            .getValue().doubleValue();
    }

    private Double getLatestSkinFold(User user) {
        return user.getSkinFolds().stream().max(Comparator.
            comparing(ThicknessOfTheSkinFatFold::getCreatedAt)).get().getValue().doubleValue();
    }

    private Double getLatestVlc(User user) {
        return user.getVitalLungCapacities().stream().max(Comparator.
            comparing(VitalLungCapacity::getCreatedAt)).get().getValue().doubleValue();
    }

    private Double getLatestDynamometry(User user) {
        return user.getDynamometries().stream().max(Comparator.comparing
            (Dynamometry::getCreatedAt)).get().getValue().doubleValue();
    }

    private Double getLatestDeadlift(User user) {
        return user.getDeadliftStrengths().stream().max(Comparator
            .comparing(DeadliftStrength::getCreatedAt)).get().getValue().doubleValue();
    }

    private Double getLatestHr(User user) {
        return user.getHeartRates().stream().max(Comparator
            .comparing(HeartRate::getCreatedAt)).get().getValue().doubleValue();
    }

    private Double getLatestRr(User user) {
        return user.getRespiratoryRates().stream().max(Comparator
            .comparing(RespiratoryRate::getCreatedAt)).get().getValue().doubleValue();
    }

    private Double getLatestGlucose(User user) {
        return user.getGlucoseLevels().stream().max(Comparator
            .comparing(GlucoseLevel::getCreatedAt)).get().getValue();
    }

    private Double getLatestCholesterol(User user) {
        return user.getCholesterolLevels().stream().max(Comparator
            .comparing(CholesterolLevel::getCreatedAt)).get().getValue();
    }

    private Double getLatestTriglyceride(User user) {
        return user.getTriglycerideLevels().stream().max(Comparator
            .comparing(TriglycerideLevel::getCreatedAt)).get().getValue();
    }

    private Double getLatestIgg(User user) {
        return user.getImmunoglobulinLevels().stream().max(Comparator
            .comparing(LevelOfImmunoglobulins::getCreatedAt)).get().getValue();
    }

    private Double getLatestPhagocytic(User user) {
        return user.getPhagocyticIndexes().stream().max(Comparator
            .comparing(PhagocyticIndex::getCreatedAt)).get().getValue();
    }

    private Double getLatestStress(User user) {
        return user.getStressLevels().stream().max(Comparator
            .comparing(StressLevel::getCreatedAt)).get().getValue().doubleValue();
    }

    private Double getLatestWellBeing(User user) {
        return user.getSubjectiveWellBeings().stream().max(Comparator
            .comparing(SubjectiveWellBeing::getCreatedAt)).get().getValue().doubleValue();
    }

    private Double average(List<Double> list) {
        return list.isEmpty() ? null : list.stream().mapToDouble
                (Double::doubleValue).average().orElse(0);
    }

    private Double evaluateBmi(Double heightCm, Double weightKg, int age) {
        if (heightCm == null || weightKg == null || heightCm <= 0) {
            return null;
        }
        double heightM = heightCm / 100.0;
        double bmi = weightKg / (heightM * heightM);

        HealthNormsProperties.StandardNorm norm = norms.getBmiNorm(age);
        if (norm == null) {
            return null;
        }
        return mapToTenPointScale(bmi, norm.getIdeal(), norm.getMin(), norm.getMax(), 1);
    }

    private Double evaluateChestCircumference(User user, int age, String gender) {
        if (user.getChestCircumferences() == null ||
                user.getChestCircumferences().isEmpty()) {
            return null;
        }
        double value = user.getChestCircumferences().stream()
                .max(Comparator.comparing(ChestCircumference::getCreatedAt)).get().getValue();

        HealthNormsProperties.ChestNorm norm = norms.getChestNorm(age);
        if (norm == null) {
            return null;
        }
        boolean isMale = "male".equalsIgnoreCase(gender);
        double ideal = isMale ? norm.getMaleIdeal() : norm.getFemaleIdeal();
        double min = isMale ? norm.getMaleMin() : norm.getFemaleMin();
        double max = isMale ? norm.getMaleMax() : norm.getFemaleMax();

        return mapToTenPointScale(value, ideal, min, max, 1);
    }

    private Double evaluateSkinFold(User user, int age) {
        if (user.getSkinFolds() == null || user.getSkinFolds().isEmpty()) {
            return null;
        }
        double value = user.getSkinFolds().stream()
                .max(Comparator.comparing(ThicknessOfTheSkinFatFold::getCreatedAt)).get().getValue();

        HealthNormsProperties.StandardNorm norm = norms.getSkinFoldNorm(age);
        if (norm == null) {
            return null;
        }
        return mapToTenPointScale(value, norm.getIdeal(), norm.getMin(), norm.getMax(), 1);
    }

    private Double evaluateVitalLungCapacity(User user, int age) {
        if (user.getVitalLungCapacities() == null || user.getVitalLungCapacities().isEmpty()) {
            return null;
        }
        double value = user.getVitalLungCapacities().stream()
                .max(Comparator.comparing(VitalLungCapacity::getCreatedAt)).get().getValue();

        HealthNormsProperties.StandardNorm norm = norms.getVitalLungCapacityNorm(age);
        if (norm == null) {
            return null;
        }
        return mapToTenPointScale(value, norm.getIdeal(), norm.getMin(), norm.getMax(), 1);
    }

    private Double evaluateDynamometry(User user, int age, String gender) {
        if (user.getDynamometries() == null || user.getDynamometries().isEmpty()) {
            return null;
        }
        double value = user.getDynamometries().stream()
                .max(Comparator.comparing(Dynamometry::getCreatedAt)).get().getValue();

        HealthNormsProperties.DynamometryNorm norm = norms.getDynamometryNorm(age);
        if (norm == null) {
            return null;
        }
        boolean isMale = "male".equalsIgnoreCase(gender);
        double ideal = isMale ? norm.getMaleIdeal() : norm.getFemaleIdeal();
        double min = isMale ? norm.getMaleMin() : norm.getFemaleMin();
        double max = isMale ? norm.getMaleMax() : norm.getFemaleMax();

        return mapToTenPointScale(value, ideal, min, max, 0);
    }

    private Double evaluateDeadliftStrength(User user, int age, Double weight) {
        if (user.getDeadliftStrengths() == null || user.getDeadliftStrengths().isEmpty() || weight == null) {
            return null;
        }
        double value = user.getDeadliftStrengths().stream()
                .max(Comparator.comparing(DeadliftStrength::getCreatedAt)).get().getValue();

        HealthNormsProperties.RatioNorm norm = norms.getDeadliftStrengthNorm(age);
        if (norm == null) {
            return null;
        }
        double ideal = weight * norm.getIdealRatio();
        double min = weight * norm.getMinRatio();
        double max = weight * norm.getMaxRatio();

        return mapToTenPointScale(value, ideal, min, max, 0);
    }

    private Double evaluateHeartRate(User user, int age) {
        if (user.getHeartRates() == null || user.getHeartRates().isEmpty()) {
            return null;
        }
        double value = user.getHeartRates().stream()
                .max(Comparator.comparing(HeartRate::getCreatedAt)).get().getValue();

        HealthNormsProperties.StandardNorm norm = norms.getHeartRateNorm(age);
        if (norm == null) {
            return null;
        }
        return mapToTenPointScale(value, norm.getIdeal(), norm.getMin(), norm.getMax(), 0);
    }

    private Double evaluateBloodPressure(User user, int age) {
        if (user.getBloodPressures() == null || user.getBloodPressures().isEmpty()) {
            return null;
        }
        BloodPressure bp = user.getBloodPressures().stream()
                .max(Comparator.comparing(BloodPressure::getCreatedAt)).get();

        HealthNormsProperties.BloodPressureNorm norm = norms.getBloodPressureNorm(age);
        if (norm == null) {
            return null;
        }

        double sysScore = mapToTenPointScale(bp.getUpperPressure(),
                norm.getSysIdeal(), norm.getSysMin(), norm.getSysMax(), 1);
        double diaScore = mapToTenPointScale(bp.getLowerPressure(),
                norm.getDiaIdeal(), norm.getDiaMin(), norm.getDiaMax(), 1);
        return (sysScore + diaScore) / 2.0;
    }

    private Double evaluateRespiratoryRate(User user, int age) {
        if (user.getRespiratoryRates() == null || user.getRespiratoryRates().isEmpty()) {
            return null;
        }
        double value = user.getRespiratoryRates().stream()
                .max(Comparator.comparing(RespiratoryRate::getCreatedAt)).get().getValue();

        HealthNormsProperties.StandardNorm norm = norms.getRespiratoryRateNorm(age);
        if (norm == null) {
            return null;
        }
        return mapToTenPointScale(value, norm.getIdeal(), norm.getMin(), norm.getMax(), 1);
    }

    private Double evaluateGlucose(User user, int age) {
        if (user.getGlucoseLevels() == null || user.getGlucoseLevels().isEmpty()) {
            return null;
        }
        double value = user.getGlucoseLevels().stream()
                .max(Comparator.comparing(GlucoseLevel::getCreatedAt)).get().getValue();

        HealthNormsProperties.StandardNorm norm = norms.getGlucoseNorm(age);
        if (norm == null) {
            return null;
        }
        return mapToTenPointScale(value, norm.getIdeal(), norm.getMin(), norm.getMax(), 1);
    }

    private Double evaluateCholesterol(User user, int age) {
        if (user.getCholesterolLevels() == null || user.getCholesterolLevels().isEmpty()) {
            return null;
        }
        double value = user.getCholesterolLevels().stream()
                .max(Comparator.comparing(CholesterolLevel::getCreatedAt)).get().getValue();

        HealthNormsProperties.StandardNorm norm = norms.getCholesterolNorm(age);
        if (norm == null) {
            return null;
        }
        return mapToTenPointScale(value, norm.getIdeal(), norm.getMin(), norm.getMax(), 1);
    }

    private Double evaluateTriglyceride(User user, int age) {
        if (user.getTriglycerideLevels() == null || user.getTriglycerideLevels().isEmpty()) {
            return null;
        }
        double value = user.getTriglycerideLevels().stream()
                .max(Comparator.comparing(TriglycerideLevel::getCreatedAt)).get().getValue();

        HealthNormsProperties.StandardNorm norm = norms.getTriglycerideNorm(age);
        if (norm == null) {
            return null;
        }
        return mapToTenPointScale(value, norm.getIdeal(), norm.getMin(), norm.getMax(), 1);
    }

    private Double evaluateImmunoglobulinG(User user, int age) {
        if (user.getImmunoglobulinLevels() == null || user.getImmunoglobulinLevels().isEmpty()) {
            return null;
        }
        double value = user.getImmunoglobulinLevels().stream()
                .max(Comparator.comparing(LevelOfImmunoglobulins::getCreatedAt)).get().getValue();

        HealthNormsProperties.StandardNorm norm = norms.getImmunoglobulinGNorm(age);
        if (norm == null) {
            return null;
        }
        return mapToTenPointScale(value, norm.getIdeal(), norm.getMin(), norm.getMax(), 1);
    }

    private Double evaluatePhagocyticIndex(User user, int age) {
        if (user.getPhagocyticIndexes() == null || user.getPhagocyticIndexes().isEmpty()) {
            return null;
        }
        double value = user.getPhagocyticIndexes().stream()
                .max(Comparator.comparing(PhagocyticIndex::getCreatedAt)).get().getValue();

        HealthNormsProperties.StandardNorm norm = norms.getPhagocyticIndexNorm(age);
        if (norm == null) {
            return null;
        }
        return mapToTenPointScale(value, norm.getIdeal(), norm.getMin(), norm.getMax(), 1);
    }

    private Double evaluateStress(User user, int age) {
        if (user.getStressLevels() == null || user.getStressLevels().isEmpty()) {
            return null;
        }
        double value = user.getStressLevels().stream()
                .max(Comparator.comparing(StressLevel::getCreatedAt)).get().getValue();

        HealthNormsProperties.StandardNorm norm = norms.getStressNorm(age);
        if (norm == null) {
            return null;
        }
        return mapToTenPointScale(value, norm.getIdeal(), norm.getMin(), norm.getMax(), 0);
    }

    private Double evaluateMentalDisorders(User user) {
        // Если нет ни расстройств, ни данных о стрессе - не учитываем категорию
        if ((user.getMentalDisorders() == null || user.getMentalDisorders().isEmpty()) &&
                (user.getStressLevels() == null || user.getStressLevels().isEmpty())) {
            return null;
        }

        // Если данные о стрессе есть, а расстройств нет - психика в порядке (10)
        if (user.getMentalDisorders() == null || user.getMentalDisorders().isEmpty()) {
            return 10.0;
        }

        // Если есть хотя бы одно расстройство - низкий балл
        return 2.0;
    }

    private Double evaluateWellBeing(User user, int age) {
        if (user.getSubjectiveWellBeings() == null || user.getSubjectiveWellBeings().isEmpty()) {
            return null;
        }
        double value = user.getSubjectiveWellBeings().stream()
                .max(Comparator.comparing(SubjectiveWellBeing::getCreatedAt)).get().getValue();

        HealthNormsProperties.StandardNorm norm = norms.getSubjectiveWellBeingNorm(age);
        if (norm == null) {
            return null;
        }
        return mapToTenPointScale(value, norm.getIdeal(), norm.getMin(), norm.getMax(), 0);
    }

    private double mapToTenPointScale(double actual, double ideal, double min, double max, int mode) {
        if (ideal == min && ideal == max) {
            return 10.0;
        }
        if (actual == ideal) {
            return 10.0;
        }

        double deviationPercent = 0.0;

        if (actual < min) {
            if (mode == 0) {
                return 10.0;
            }
            deviationPercent = ((min - actual) / (ideal - min)) * 100.0;
        } else if (actual > max) {
            deviationPercent = ((actual - max) / (max - ideal)) * 100.0;
        } else {
            if (actual < ideal) {
                deviationPercent = ((ideal - actual) / (ideal - min)) * 50.0;
            } else {
                deviationPercent = ((actual - ideal) / (max - ideal)) * 50.0;
            }
        }

        double score = 10.0 - (deviationPercent / 10.0);
        return Math.max(0.0, Math.min(10.0, score));
    }

    private String interpretScore(double score) {
        if (score >= 8.0) {
            return "Здоровый";
        }
        if (score >= 4.0) {
            return "Среднее состояние";
        }
        return "Больной";
    }
}
