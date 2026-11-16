package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.DeadliftStrengthDto;
import ru.ulstu.health.entity.DeadliftStrength;
import ru.ulstu.health.mapper.DeadliftStrengthMapper;
import ru.ulstu.health.service.DeadliftStrengthService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/deadlift-strengths")
@RequiredArgsConstructor
public class DeadliftStrengthController {

    private final DeadliftStrengthService service;
    private final DeadliftStrengthMapper mapper;

    @GetMapping
    public List<DeadliftStrengthDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeadliftStrengthDto create(@PathVariable String userId, @Valid @RequestBody DeadliftStrengthDto dto) {
        DeadliftStrength entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public DeadliftStrengthDto update(@PathVariable String userId, @PathVariable String id, @Valid @RequestBody DeadliftStrengthDto dto) {
        DeadliftStrength entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
