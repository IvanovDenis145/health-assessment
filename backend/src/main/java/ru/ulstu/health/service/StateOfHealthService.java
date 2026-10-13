package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.StateOfHealth;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.StateOfHealthRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateOfHealthService {

    private final StateOfHealthRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<StateOfHealth> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public StateOfHealth findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("StateOfHealth", id));
    }

    @Transactional
    public StateOfHealth create(String userId, StateOfHealth entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public StateOfHealth update(String id, StateOfHealth updated) {
        StateOfHealth existing = findById(id);
        existing.setValue(updated.getValue());
        existing.setName(updated.getName());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("StateOfHealth", id);
        }
        repository.deleteById(id);
    }
}