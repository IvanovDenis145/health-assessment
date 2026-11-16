package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.BloodPressureDto;
import ru.ulstu.health.entity.BloodPressure;
import ru.ulstu.health.mapper.BloodPressureMapper;
import ru.ulstu.health.service.BloodPressureService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/blood-pressures")
@RequiredArgsConstructor
public class BloodPressureController {

    private final BloodPressureService service;
    private final BloodPressureMapper mapper;

    @GetMapping
    public List<BloodPressureDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BloodPressureDto create(@PathVariable String userId, @Valid @RequestBody BloodPressureDto dto) {
        BloodPressure entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public BloodPressureDto update(@PathVariable String userId,
                                   @PathVariable String id, @Valid @RequestBody BloodPressureDto dto) {
        BloodPressure entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
