package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.GlucoseLevelDto;
import ru.ulstu.health.entity.GlucoseLevel;
import ru.ulstu.health.mapper.GlucoseLevelMapper;
import ru.ulstu.health.service.GlucoseLevelService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/glucose-levels")
@RequiredArgsConstructor
public class GlucoseLevelController {

    private final GlucoseLevelService service;
    private final GlucoseLevelMapper mapper;

    @GetMapping
    public List<GlucoseLevelDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GlucoseLevelDto create(@PathVariable String userId, @Valid @RequestBody GlucoseLevelDto dto) {
        GlucoseLevel entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public GlucoseLevelDto update(@PathVariable String userId, @PathVariable String id, @Valid @RequestBody GlucoseLevelDto dto) {
        GlucoseLevel entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
