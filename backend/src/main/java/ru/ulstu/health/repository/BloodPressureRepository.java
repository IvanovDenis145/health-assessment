package ru.ulstu.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ulstu.health.entity.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface BloodPressureRepository extends JpaRepository<BloodPressure, String> {
    List<BloodPressure> findAllByUserId(String userId);
}
