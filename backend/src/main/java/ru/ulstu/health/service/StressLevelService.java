package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.StressLevel;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.StressLevelRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StressLevelService {

    private final StressLevelRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<StressLevel> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public StressLevel findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StressLevel", id));
    }

    @Transactional
    public StressLevel create(String userId, StressLevel entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public StressLevel update(String id, StressLevel updated) {
        StressLevel existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("StressLevel", id);
        }
        repository.deleteById(id);
    }
}