package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.TriglycerideLevel;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.TriglycerideLevelRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TriglycerideLevelService {

    private final TriglycerideLevelRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<TriglycerideLevel> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public TriglycerideLevel findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TriglycerideLevel", id));
    }

    @Transactional
    public TriglycerideLevel create(String userId, TriglycerideLevel entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public TriglycerideLevel update(String id, TriglycerideLevel updated) {
        TriglycerideLevel existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("TriglycerideLevel", id);
        }
        repository.deleteById(id);
    }
}