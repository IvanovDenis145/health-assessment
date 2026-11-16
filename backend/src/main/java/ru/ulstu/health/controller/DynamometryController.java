package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.DynamometryDto;
import ru.ulstu.health.entity.Dynamometry;
import ru.ulstu.health.mapper.DynamometryMapper;
import ru.ulstu.health.service.DynamometryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/dynamometries")
@RequiredArgsConstructor
public class DynamometryController {

    private final DynamometryService service;
    private final DynamometryMapper mapper;

    @GetMapping
    public List<DynamometryDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DynamometryDto create(@PathVariable String userId, @Valid @RequestBody DynamometryDto dto) {
        Dynamometry entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public DynamometryDto update(@PathVariable String userId,
                                 @PathVariable String id, @Valid @RequestBody DynamometryDto dto) {
        Dynamometry entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
