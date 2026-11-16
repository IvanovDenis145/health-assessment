package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.user.JwtAuthResponseDto;
import ru.ulstu.health.dto.user.LoginRequestDto;
import ru.ulstu.health.dto.user.UserCreateDto;
import ru.ulstu.health.dto.user.UserDto;
import ru.ulstu.health.entity.User;
import ru.ulstu.health.mapper.UserMapper;
import ru.ulstu.health.security.JwtTokenProvider;
import ru.ulstu.health.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserCreateDto dto) {
        User user = userMapper.toEntity(dto);
        User savedUser = userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(savedUser));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponseDto> login(@Valid @RequestBody LoginRequestDto dto) {
        // 1. Аутентификация (Spring Security сам проверит пароль через BCrypt)
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 2. Генерация JWT токена
        String token = tokenProvider.generateToken(authentication);

        // 3. Получение данных пользователя для ответа
        User user = userService.findByEmail(dto.getEmail());
        UserDto userDto = userMapper.toDto(user);

        return ResponseEntity.ok(new JwtAuthResponseDto(token, "Bearer", userDto));
    }
}
