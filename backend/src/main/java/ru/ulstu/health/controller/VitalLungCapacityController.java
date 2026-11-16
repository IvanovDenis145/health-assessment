package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.VitalLungCapacityDto;
import ru.ulstu.health.entity.VitalLungCapacity;
import ru.ulstu.health.mapper.VitalLungCapacityMapper;
import ru.ulstu.health.service.VitalLungCapacityService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/vital-lung-capacities")
@RequiredArgsConstructor
public class VitalLungCapacityController {

    private final VitalLungCapacityService service;
    private final VitalLungCapacityMapper mapper;

    @GetMapping
    public List<VitalLungCapacityDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VitalLungCapacityDto create(@PathVariable String userId, @Valid @RequestBody VitalLungCapacityDto dto) {
        VitalLungCapacity entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public VitalLungCapacityDto update(@PathVariable String userId, @PathVariable String id, @Valid @RequestBody VitalLungCapacityDto dto) {
        VitalLungCapacity entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
