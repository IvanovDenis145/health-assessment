package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.LevelOfImmunoglobulinsDto;
import ru.ulstu.health.entity.LevelOfImmunoglobulins;
import ru.ulstu.health.mapper.LevelOfImmunoglobulinsMapper;
import ru.ulstu.health.service.LevelOfImmunoglobulinsService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/immunoglobulin-levels")
@RequiredArgsConstructor
public class LevelOfImmunoglobulinsController {

    private final LevelOfImmunoglobulinsService service;
    private final LevelOfImmunoglobulinsMapper mapper;

    @GetMapping
    public List<LevelOfImmunoglobulinsDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public LevelOfImmunoglobulinsDto create(@PathVariable String userId, @Valid @RequestBody LevelOfImmunoglobulinsDto dto) {
        LevelOfImmunoglobulins entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public LevelOfImmunoglobulinsDto update(@PathVariable String userId, @PathVariable String id, @Valid @RequestBody LevelOfImmunoglobulinsDto dto) {
        LevelOfImmunoglobulins entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
