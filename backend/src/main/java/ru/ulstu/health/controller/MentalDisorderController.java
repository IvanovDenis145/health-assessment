package ru.ulstu.health.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.MentalDisorderDto;
import ru.ulstu.health.entity.MentalDisorder;
import ru.ulstu.health.mapper.MentalDisorderMapper;
import ru.ulstu.health.service.MentalDisorderService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/mental-disorders")
@RequiredArgsConstructor
public class MentalDisorderController {

    private final MentalDisorderService service;
    private final MentalDisorderMapper mapper;

    @GetMapping
    public List<MentalDisorderDto> getAll(@PathVariable String userId) {
        return service.findAllByUser(userId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MentalDisorderDto create(@PathVariable String userId, @Valid @RequestBody MentalDisorderDto dto) {
        MentalDisorder entity = mapper.toEntity(dto);
        return mapper.toDto(service.create(userId, entity));
    }

    @PutMapping("/{id}")
    public MentalDisorderDto update(@PathVariable String userId,
                                    @PathVariable String id, @Valid @RequestBody MentalDisorderDto dto) {
        MentalDisorder entity = mapper.toEntity(dto);
        return mapper.toDto(service.update(id, entity));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String userId, @PathVariable String id) {
        service.delete(id);
    }
}
