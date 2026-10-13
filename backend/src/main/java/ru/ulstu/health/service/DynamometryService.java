package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.Dynamometry;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.DynamometryRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DynamometryService {

    private final DynamometryRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Dynamometry> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Dynamometry findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dynamometry", id));
    }

    @Transactional
    public Dynamometry create(String userId, Dynamometry entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public Dynamometry update(String id, Dynamometry updated) {
        Dynamometry existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Dynamometry", id);
        }
        repository.deleteById(id);
    }
}
