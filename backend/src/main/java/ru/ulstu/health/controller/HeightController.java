package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.HeightDto;
import ru.ulstu.health.entity.Height;
import ru.ulstu.health.mapper.HeightMapper;
import ru.ulstu.health.service.HeightService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/heights")
@RequiredArgsConstructor
public class HeightController {

    private final HeightService heightService;
    private final HeightMapper heightMapper;

    @GetMapping
    public List<HeightDto> getAllHeights(@PathVariable String userId) {
        return heightService.findAllByUser(userId).stream()
                .map(heightMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HeightDto createHeight(@PathVariable String userId, @Valid @RequestBody HeightDto dto) {
        Height height = heightMapper.toEntity(dto);
        Height saved = heightService.create(userId, height);
        return heightMapper.toDto(saved);
    }

    @PutMapping("/{id}")
    public HeightDto updateHeight(@PathVariable String userId, @PathVariable String id, @Valid @RequestBody HeightDto dto) {
        Height height = heightMapper.toEntity(dto);
        Height updated = heightService.update(id, height);
        return heightMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteHeight(@PathVariable String userId, @PathVariable String id) {
        heightService.delete(id);
    }
}
