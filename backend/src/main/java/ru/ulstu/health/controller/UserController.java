package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.user.UserCreateDto;
import ru.ulstu.health.dto.user.UserDto;
import ru.ulstu.health.dto.user.UserUpdateDto;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.mapper.UserMapper;
import ru.ulstu.health.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable String id) {
        return userMapper.toDto(userService.findById(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody UserCreateDto createDto) {
        User user = userMapper.toEntity(createDto);
        User savedUser = userService.create(user);
        return userMapper.toDto(savedUser);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(@PathVariable String id, @Valid @RequestBody UserUpdateDto updateDto) {
        User existing = userService.findById(id);
        userMapper.updateEntityFromDto(updateDto, existing);
        User updated = userService.update(id, existing);
        return userMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable String id) {
        userService.delete(id);
    }
}
