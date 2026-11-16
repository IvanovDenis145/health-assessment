package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.StateOfHealthDto;
import ru.ulstu.health.entity.StateOfHealth;
import ru.ulstu.health.mapper.StateOfHealthMapper;
import ru.ulstu.health.service.StateOfHealthService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/state-of-healths")
@RequiredArgsConstructor
public class StateOfHealthController {

    private final StateOfHealthService service;
    private final StateOfHealthMapper mapper;

    @GetMapping
    public List<StateOfHealthDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public StateOfHealthDto create(@PathVariable String userId, @Valid @RequestBody StateOfHealthDto dto) {
        StateOfHealth entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public StateOfHealthDto update(@PathVariable String userId,
                                   @PathVariable String id, @Valid @RequestBody StateOfHealthDto dto) {
        StateOfHealth entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
