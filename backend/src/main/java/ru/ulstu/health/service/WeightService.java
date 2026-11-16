package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.Weight;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.WeightRepository;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WeightService {

    private final WeightRepository weightRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<Weight> findAllByUser(String userId) {
        return weightRepository.findAllByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Weight findById(String id) {
        return weightRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Weight", id));
    }

    @Transactional
    public Weight create(String userId, Weight weight) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", userId));
        weight.setUser(user);
        return weightRepository.save(weight);
    }

    @Transactional
    public Weight update(String id, Weight updated) {
        Weight existing = findById(id);
        existing.setValue(updated.getValue());
        return weightRepository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if (!weightRepository.existsById(id)) {
            throw new ResourceNotFoundException("Weight", id);
        }
        weightRepository.deleteById(id);
    }
}
