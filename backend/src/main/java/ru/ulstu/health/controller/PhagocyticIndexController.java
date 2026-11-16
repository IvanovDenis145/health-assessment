package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.PhagocyticIndexDto;
import ru.ulstu.health.entity.PhagocyticIndex;
import ru.ulstu.health.mapper.PhagocyticIndexMapper;
import ru.ulstu.health.service.PhagocyticIndexService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/phagocytic-indexes")
@RequiredArgsConstructor
public class PhagocyticIndexController {

    private final PhagocyticIndexService service;
    private final PhagocyticIndexMapper mapper;

    @GetMapping
    public List<PhagocyticIndexDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PhagocyticIndexDto create(@PathVariable String userId, @Valid @RequestBody PhagocyticIndexDto dto) {
        PhagocyticIndex entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public PhagocyticIndexDto update(@PathVariable String userId,
                                     @PathVariable String id, @Valid @RequestBody PhagocyticIndexDto dto) {
        PhagocyticIndex entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
