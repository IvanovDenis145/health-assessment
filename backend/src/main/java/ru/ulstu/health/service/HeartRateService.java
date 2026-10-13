package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.HeartRate;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.HeartRateRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeartRateService {

    private final HeartRateRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<HeartRate> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public HeartRate findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HeartRate", id));
    }

    @Transactional
    public HeartRate create(String userId, HeartRate entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public HeartRate update(String id, HeartRate updated) {
        HeartRate existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("HeartRate", id);
        }
        repository.deleteById(id);
    }
}
