package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.BloodPressure;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.BloodPressureRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BloodPressureService {

    private final BloodPressureRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<BloodPressure> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public BloodPressure findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BloodPressure", id));
    }

    @Transactional
    public BloodPressure create(String userId, BloodPressure entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public BloodPressure update(String id, BloodPressure updated) {
        BloodPressure existing = findById(id);
        existing.setUpperPressure(updated.getUpperPressure());
        existing.setLowerPressure(updated.getLowerPressure());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("BloodPressure", id);
        }
        repository.deleteById(id);
    }
}
