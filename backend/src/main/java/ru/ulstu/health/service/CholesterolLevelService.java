package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.CholesterolLevel;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.CholesterolLevelRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CholesterolLevelService {

    private final CholesterolLevelRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<CholesterolLevel> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public CholesterolLevel findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("CholesterolLevel", id));
    }

    @Transactional
    public CholesterolLevel create(String userId, CholesterolLevel entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public CholesterolLevel update(String id, CholesterolLevel updated) {
        CholesterolLevel existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("CholesterolLevel", id);
        }
        repository.deleteById(id);
    }
}