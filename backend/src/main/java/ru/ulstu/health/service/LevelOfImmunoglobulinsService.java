package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.LevelOfImmunoglobulins;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.LevelOfImmunoglobulinsRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LevelOfImmunoglobulinsService {

    private final LevelOfImmunoglobulinsRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<LevelOfImmunoglobulins> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public LevelOfImmunoglobulins findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("LevelOfImmunoglobulins", id));
    }

    @Transactional
    public LevelOfImmunoglobulins create(String userId, LevelOfImmunoglobulins entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public LevelOfImmunoglobulins update(String id, LevelOfImmunoglobulins updated) {
        LevelOfImmunoglobulins existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("LevelOfImmunoglobulins", id);
        }
        repository.deleteById(id);
    }
}