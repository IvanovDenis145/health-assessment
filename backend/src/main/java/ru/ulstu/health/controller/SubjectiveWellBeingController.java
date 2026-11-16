package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.SubjectiveWellBeingDto;
import ru.ulstu.health.entity.SubjectiveWellBeing;
import ru.ulstu.health.mapper.SubjectiveWellBeingMapper;
import ru.ulstu.health.service.SubjectiveWellBeingService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/subjective-well-beings")
@RequiredArgsConstructor
public class SubjectiveWellBeingController {

    private final SubjectiveWellBeingService service;
    private final SubjectiveWellBeingMapper mapper;

    @GetMapping
    public List<SubjectiveWellBeingDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SubjectiveWellBeingDto create(@PathVariable String userId, @Valid @RequestBody SubjectiveWellBeingDto dto) {
        SubjectiveWellBeing entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public SubjectiveWellBeingDto update(@PathVariable String userId,
                                         @PathVariable String id, @Valid @RequestBody SubjectiveWellBeingDto dto) {
        SubjectiveWellBeing entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
