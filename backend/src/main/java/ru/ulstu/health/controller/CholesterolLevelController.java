package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.CholesterolLevelDto;
import ru.ulstu.health.entity.CholesterolLevel;
import ru.ulstu.health.mapper.CholesterolLevelMapper;
import ru.ulstu.health.service.CholesterolLevelService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/cholesterol-levels")
@RequiredArgsConstructor
public class CholesterolLevelController {

    private final CholesterolLevelService service;
    private final CholesterolLevelMapper mapper;

    @GetMapping
    public List<CholesterolLevelDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CholesterolLevelDto create(@PathVariable String userId, @Valid @RequestBody CholesterolLevelDto dto) {
        CholesterolLevel entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public CholesterolLevelDto update(@PathVariable String userId,
                                      @PathVariable String id, @Valid @RequestBody CholesterolLevelDto dto) {
        CholesterolLevel entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
