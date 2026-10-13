package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.ThicknessOfTheSkinFatFold;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.ThicknessOfTheSkinFatFoldRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThicknessOfTheSkinFatFoldService {

    private final ThicknessOfTheSkinFatFoldRepository repository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<ThicknessOfTheSkinFatFold> findAllByUser(String userId) {
        return repository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public ThicknessOfTheSkinFatFold findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ThicknessOfTheSkinFatFold", id));
    }

    @Transactional
    public ThicknessOfTheSkinFatFold create(String userId, ThicknessOfTheSkinFatFold entity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        entity.setUser(user);
        return repository.save(entity);
    }

    @Transactional
    public ThicknessOfTheSkinFatFold update(String id, ThicknessOfTheSkinFatFold updated) {
        ThicknessOfTheSkinFatFold existing = findById(id);
        existing.setValue(updated.getValue());
        return repository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("ThicknessOfTheSkinFatFold", id);
        }
        repository.deleteById(id);
    }
}
