package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.SubjectiveWellBeing;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.SubjectiveWellBeingRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubjectiveWellBeingService {

    private final SubjectiveWellBeingRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<SubjectiveWellBeing> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public SubjectiveWellBeing findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubjectiveWellBeing", id));
    }

    @Transactional
    public SubjectiveWellBeing create(String userId, SubjectiveWellBeing entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public SubjectiveWellBeing update(String id, SubjectiveWellBeing updated) {
        SubjectiveWellBeing existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("SubjectiveWellBeing", id);
        }
        repository.deleteById(id);
    }
}