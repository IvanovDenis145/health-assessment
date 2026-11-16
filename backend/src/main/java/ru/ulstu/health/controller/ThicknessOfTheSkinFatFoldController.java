package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.ThicknessOfTheSkinFatFoldDto;
import ru.ulstu.health.entity.ThicknessOfTheSkinFatFold;
import ru.ulstu.health.mapper.ThicknessOfTheSkinFatFoldMapper;
import ru.ulstu.health.service.ThicknessOfTheSkinFatFoldService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/skin-folds")
@RequiredArgsConstructor
public class ThicknessOfTheSkinFatFoldController {

    private final ThicknessOfTheSkinFatFoldService service;
    private final ThicknessOfTheSkinFatFoldMapper mapper;

    @GetMapping
    public List<ThicknessOfTheSkinFatFoldDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ThicknessOfTheSkinFatFoldDto create(@PathVariable String userId,
                                               @Valid @RequestBody
                                               ThicknessOfTheSkinFatFoldDto dto) {
        ThicknessOfTheSkinFatFold entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public ThicknessOfTheSkinFatFoldDto update(@PathVariable String userId,
                                               @PathVariable String id,
                                               @Valid @RequestBody
                                                   ThicknessOfTheSkinFatFoldDto dto) {
        ThicknessOfTheSkinFatFold entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
