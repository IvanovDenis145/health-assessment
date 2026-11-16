package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.ChestCircumference;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.ChestCircumferenceRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChestCircumferenceService {

    private final ChestCircumferenceRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ChestCircumference> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public ChestCircumference findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ChestCircumference", id));
    }

    @Transactional
    public ChestCircumference create(String userId, ChestCircumference entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public ChestCircumference update(String id, ChestCircumference updated) {
        ChestCircumference existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ChestCircumference", id);
        }
        repository.deleteById(id);
    }
}
