package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.ChestCircumferenceDto;
import ru.ulstu.health.entity.ChestCircumference;
import ru.ulstu.health.mapper.ChestCircumferenceMapper;
import ru.ulstu.health.service.ChestCircumferenceService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/chest-circumferences")
@RequiredArgsConstructor
public class ChestCircumferenceController {

    private final ChestCircumferenceService service;
    private final ChestCircumferenceMapper mapper;

    @GetMapping
    public List<ChestCircumferenceDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChestCircumferenceDto create(@PathVariable String userId, @Valid @RequestBody ChestCircumferenceDto dto) {
        ChestCircumference entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public ChestCircumferenceDto update(@PathVariable String userId, @PathVariable String id, @Valid @RequestBody ChestCircumferenceDto dto) {
        ChestCircumference entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
