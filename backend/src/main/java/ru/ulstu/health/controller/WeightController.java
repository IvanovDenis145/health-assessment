package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.WeightDto;
import ru.ulstu.health.entity.Weight;
import ru.ulstu.health.mapper.WeightMapper;
import ru.ulstu.health.service.WeightService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/weights")
@RequiredArgsConstructor
public class WeightController {

    private final WeightService weightService;
    private final WeightMapper weightMapper;

    @GetMapping
    public List<WeightDto> getAll(@PathVariable String userId) {
        return weightService.findAllByUser(userId).stream()
                .map(weightMapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WeightDto create(@PathVariable String userId, @Valid @RequestBody WeightDto dto) {
        Weight entity = weightMapper.toEntity(dto);
        return weightMapper.toDto(weightService.create(userId, entity));
    }

    @PutMapping("/{id}")
    public WeightDto update(@PathVariable String userId, @PathVariable String id, @Valid @RequestBody WeightDto dto) {
        Weight entity = weightMapper.toEntity(dto);
        return weightMapper.toDto(weightService.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        weightService.delete(id);
    }
}
