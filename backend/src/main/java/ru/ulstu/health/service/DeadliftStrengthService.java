package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.DeadliftStrength;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.DeadliftStrengthRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeadliftStrengthService {

    private final DeadliftStrengthRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<DeadliftStrength> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public DeadliftStrength findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DeadliftStrength", id));
    }

    @Transactional
    public DeadliftStrength create(String userId, DeadliftStrength entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public DeadliftStrength update(String id, DeadliftStrength updated) {
        DeadliftStrength existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("DeadliftStrength", id);
        }
        repository.deleteById(id);
    }
}
