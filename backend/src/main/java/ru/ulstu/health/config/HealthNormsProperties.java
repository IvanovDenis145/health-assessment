package ru.ulstu.health.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "health.norms")
public class HealthNormsProperties {

    private List<StandardNorm> bmi;
    private List<ChestNorm> chestCircumference;
    private List<StandardNorm> skinFold;
    private List<StandardNorm> vitalLungCapacity;
    private List<DynamometryNorm> dynamometry;
    private List<RatioNorm> deadliftStrength;
    private List<StandardNorm> heartRate;
    private List<BloodPressureNorm> bloodPressure;
    private List<StandardNorm> respiratoryRate;
    private List<StandardNorm> glucoseLevel;
    private List<StandardNorm> cholesterolLevel;
    private List<StandardNorm> triglycerideLevel;
    private List<StandardNorm> immunoglobulinG;
    private List<StandardNorm> phagocyticIndex;
    private List<StandardNorm> stressLevel;
    private List<StandardNorm> subjectiveWellBeing;

    @Data
    public static class StandardNorm {
        private int ageMin;
        private int ageMax;
        private double ideal;
        private double min;
        private double max;
    }

    @Data
    public static class BloodPressureNorm {
        private int ageMin;
        private int ageMax;
        private double sysIdeal;
        private double sysMin;
        private double sysMax;
        private double diaIdeal;
        private double diaMin;
        private double diaMax;
    }

    @Data
    public static class DynamometryNorm {
        private int ageMin;
        private int ageMax;
        private double maleIdeal;
        private double maleMin;
        private double maleMax;
        private double femaleIdeal;
        private double femaleMin;
        private double femaleMax;
    }

    @Data
    public static class ChestNorm {
        private int ageMin;
        private int ageMax;
        private double maleIdeal;
        private double maleMin;
        private double maleMax;
        private double femaleIdeal;
        private double femaleMin;
        private double femaleMax;
    }

    @Data
    public static class RatioNorm {
        private int ageMin;
        private int ageMax;
        private double idealRatio;
        private double minRatio;
        private double maxRatio;
    }

    public StandardNorm getBmiNorm(int age) {
        return findNorm(bmi, age);
    }

    public ChestNorm getChestNorm(int age) {
        return findNorm(chestCircumference, age);
    }

    public StandardNorm getSkinFoldNorm(int age) {
        return findNorm(skinFold, age);
    }

    public StandardNorm getVitalLungCapacityNorm(int age) {
        return findNorm(vitalLungCapacity, age);
    }

    public DynamometryNorm getDynamometryNorm(int age) {
        return findNorm(dynamometry, age);
    }

    public RatioNorm getDeadliftStrengthNorm(int age) {
        return findNorm(deadliftStrength, age);
    }

    public StandardNorm getHeartRateNorm(int age) {
        return findNorm(heartRate, age);
    }

    public BloodPressureNorm getBloodPressureNorm(int age) {
        return findNorm(bloodPressure, age);
    }

    public StandardNorm getRespiratoryRateNorm(int age) {
        return findNorm(respiratoryRate, age);
    }

    public StandardNorm getGlucoseNorm(int age) {
        return findNorm(glucoseLevel, age);
    }

    public StandardNorm getCholesterolNorm(int age) {
        return findNorm(cholesterolLevel, age);
    }

    public StandardNorm getTriglycerideNorm(int age) {
        return findNorm(triglycerideLevel, age);
    }

    public StandardNorm getImmunoglobulinGNorm(int age) {
        return findNorm(immunoglobulinG, age);
    }

    public StandardNorm getPhagocyticIndexNorm(int age) {
        return findNorm(phagocyticIndex, age);
    }

    public StandardNorm getStressNorm(int age) {
        return findNorm(stressLevel, age);
    }

    public StandardNorm getSubjectiveWellBeingNorm(int age) {
        return findNorm(subjectiveWellBeing, age);
    }

    private <T> T findNorm(List<T> norms, int age) {
        if (norms == null || norms.isEmpty()) {
            return null;
        }
        return norms.stream()
                .filter(n -> {
                    try {
                        int min = (int) n.getClass().getMethod("getAgeMin").invoke(n);
                        int max = (int) n.getClass().getMethod("getAgeMax").invoke(n);
                        return age >= min && age <= max;
                    } catch (Exception e) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(norms.get(0));
    }
}
