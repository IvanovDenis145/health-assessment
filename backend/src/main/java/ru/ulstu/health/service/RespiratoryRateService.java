package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.RespiratoryRate;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.RespiratoryRateRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RespiratoryRateService {

    private final RespiratoryRateRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<RespiratoryRate> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public RespiratoryRate findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("RespiratoryRate", id));
    }

    @Transactional
    public RespiratoryRate create(String userId, RespiratoryRate entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public RespiratoryRate update(String id, RespiratoryRate updated) {
        RespiratoryRate existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("RespiratoryRate", id);
        }
        repository.deleteById(id);
    }
}
