package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.GlucoseLevel;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.GlucoseLevelRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GlucoseLevelService {

    private final GlucoseLevelRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<GlucoseLevel> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public GlucoseLevel findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GlucoseLevel", id));
    }

    @Transactional
    public GlucoseLevel create(String userId, GlucoseLevel entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public GlucoseLevel update(String id, GlucoseLevel updated) {
        GlucoseLevel existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("GlucoseLevel", id);
        }
        repository.deleteById(id);
    }
}