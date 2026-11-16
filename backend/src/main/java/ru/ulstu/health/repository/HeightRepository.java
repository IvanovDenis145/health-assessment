package ru.ulstu.health.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.ulstu.health.entity.*;
import java.util.List;

@Repository
public interface HeightRepository extends JpaRepository<Height, String> {
    List<Height> findAllByUserId(String userId);
}
