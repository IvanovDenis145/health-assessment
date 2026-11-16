package ru.ulstu.health.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.ulstu.health.dto.indicator.StateOfHealthDto;
import ru.ulstu.health.mapper.StateOfHealthMapper;
import ru.ulstu.health.service.HealthScoringService;

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
}