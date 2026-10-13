package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.PhagocyticIndex;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.PhagocyticIndexRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhagocyticIndexService {

    private final PhagocyticIndexRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<PhagocyticIndex> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public PhagocyticIndex findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PhagocyticIndex", id));
    }

    @Transactional
    public PhagocyticIndex create(String userId, PhagocyticIndex entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public PhagocyticIndex update(String id, PhagocyticIndex updated) {
        PhagocyticIndex existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("PhagocyticIndex", id);
        }
        repository.deleteById(id);
    }
}