package ru.ulstu.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ulstu.health.entity.*;
import java.util.List;

@Repository
public interface TriglycerideLevelRepository extends JpaRepository<TriglycerideLevel, String> {
    List<TriglycerideLevel> findAllByUserId(String userId);
}
