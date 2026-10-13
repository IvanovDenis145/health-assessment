package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.VitalLungCapacity;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.VitalLungCapacityRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class VitalLungCapacityService {

    private final VitalLungCapacityRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<VitalLungCapacity> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public VitalLungCapacity findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("VitalLungCapacity", id));
    }

    @Transactional
    public VitalLungCapacity create(String userId, VitalLungCapacity entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public VitalLungCapacity update(String id, VitalLungCapacity updated) {
        VitalLungCapacity existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("VitalLungCapacity", id);
        }
        repository.deleteById(id);
    }
}
