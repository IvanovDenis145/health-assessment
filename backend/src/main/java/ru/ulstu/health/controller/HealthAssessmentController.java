package ru.ulstu.health.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.StateOfHealthDto;
import ru.ulstu.health.entity.StateOfHealth;
import ru.ulstu.health.mapper.StateOfHealthMapper;
import ru.ulstu.health.service.HealthScoringService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users/{userId}/assessment")
@RequiredArgsConstructor
public class HealthAssessmentController {

    private final HealthScoringService scoringService;
    private final StateOfHealthMapper mapper;

    @PostMapping
    public ResponseEntity<StateOfHealthDto> assessHealth(@PathVariable String userId) {
        return ResponseEntity.ok(mapper.toDto(scoringService.evaluateUserHealth(userId)));
    }

    @GetMapping
    public ResponseEntity<List<StateOfHealthDto>> getHistory(@PathVariable String userId) {
        List<StateOfHealth> history = scoringService.getAssessmentHistory(userId);
        List<StateOfHealthDto> dtos = history.stream().map(mapper::toDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
