package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.Height;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.HeightRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HeightService {

    private final HeightRepository heightRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Height> findAllByUser(String userId) {
        return heightRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Height findById(String id) {
        return heightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Height", id));
    }

    @Transactional
    public Height create(String userId, Height height) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        height.setUser(user);
        return heightRepository.save(height);
    }

    @Transactional
    public Height update(String id, Height updated) {
        Height existing = findById(id);
        existing.setValue(updated.getValue());
        return heightRepository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!heightRepository.existsById(id)) {
            throw new ResourceNotFoundException("Height", id);
        }
        heightRepository.deleteById(id);
    }
}
