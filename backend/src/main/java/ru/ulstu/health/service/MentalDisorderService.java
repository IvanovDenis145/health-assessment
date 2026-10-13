package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.MentalDisorder;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.MentalDisorderRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MentalDisorderService {

    private final MentalDisorderRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<MentalDisorder> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public MentalDisorder findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MentalDisorder", id));
    }

    @Transactional
    public MentalDisorder create(String userId, MentalDisorder entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public MentalDisorder update(String id, MentalDisorder updated) {
        MentalDisorder existing = findById(id);
        existing.setName(updated.getName());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("MentalDisorder", id);
        }
        repository.deleteById(id);
    }
}