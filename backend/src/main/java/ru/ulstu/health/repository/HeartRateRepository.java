package ru.ulstu.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ulstu.health.entity.*;
import java.util.List;

@Repository
public interface HeartRateRepository extends JpaRepository<HeartRate, String> {
    List<HeartRate> findAllByUserId(String userId);
}
