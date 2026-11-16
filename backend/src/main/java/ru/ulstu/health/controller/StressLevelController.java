package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.StressLevelDto;
import ru.ulstu.health.entity.StressLevel;
import ru.ulstu.health.mapper.StressLevelMapper;
import ru.ulstu.health.service.StressLevelService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/stress-levels")
@RequiredArgsConstructor
public class StressLevelController {

    private final StressLevelService service;
    private final StressLevelMapper mapper;

    @GetMapping
    public List<StressLevelDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StressLevelDto create(@PathVariable String userId, @Valid @RequestBody StressLevelDto dto) {
        StressLevel entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public StressLevelDto update(@PathVariable String userId,
                                 @PathVariable String id, @Valid @RequestBody StressLevelDto dto) {
        StressLevel entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
