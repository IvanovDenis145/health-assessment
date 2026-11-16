package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.HeartRateDto;
import ru.ulstu.health.entity.HeartRate;
import ru.ulstu.health.mapper.HeartRateMapper;
import ru.ulstu.health.service.HeartRateService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/heart-rates")
@RequiredArgsConstructor
public class HeartRateController {

    private final HeartRateService service;
    private final HeartRateMapper mapper;

    @GetMapping
    public List<HeartRateDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HeartRateDto create(@PathVariable String userId, @Valid @RequestBody HeartRateDto dto) {
        HeartRate entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public HeartRateDto update(@PathVariable String userId,
                               @PathVariable String id, @Valid @RequestBody HeartRateDto dto) {
        HeartRate entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
