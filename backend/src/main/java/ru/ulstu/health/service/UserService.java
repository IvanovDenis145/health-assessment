package ru.ulstu.health.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.exception.ResourceNotFoundException;
import ru.ulstu.health.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User findById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User с email=" + email + "не найден"));
    }

    @Transactional
    public User create(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("User с email=" + user.getEmail() + " уже существует");
        }
        return userRepository.save(user);
    }

    @Transactional
    public User update(String id, User updated) {
        User existing = findById(id);
        existing.setFio(updated.getFio());
        existing.setPhoneNumber(updated.getPhoneNumber());
        existing.setEmail(updated.getEmail());
        existing.setAge(updated.getAge());
        existing.setGender(updated.getGender());
        if (updated.getPassword() != null && !updated.getPassword().isBlank()) {
            existing.setPassword(updated.getPassword());
        }
        return userRepository.save(existing);
    }

    @Transactional
    public void delete(String id) {
        if(!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", id);
        }
        userRepository.deleteById(id);
    }
}
