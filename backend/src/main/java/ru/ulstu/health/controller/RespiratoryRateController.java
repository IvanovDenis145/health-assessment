package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.RespiratoryRateDto;
import ru.ulstu.health.entity.RespiratoryRate;
import ru.ulstu.health.mapper.RespiratoryRateMapper;
import ru.ulstu.health.service.RespiratoryRateService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/respiratory-rates")
@RequiredArgsConstructor
public class RespiratoryRateController {

    private final RespiratoryRateService service;
    private final RespiratoryRateMapper mapper;

    @GetMapping
    public List<RespiratoryRateDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RespiratoryRateDto create(@PathVariable String userId, @Valid @RequestBody RespiratoryRateDto dto) {
        RespiratoryRate entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public RespiratoryRateDto update(@PathVariable String userId, @PathVariable String id, @Valid @RequestBody RespiratoryRateDto dto) {
        RespiratoryRate entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
