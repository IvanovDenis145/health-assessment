package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.DeadliftStrengthDto;
import ru.ulstu.health.entity.DeadliftStrength;

@Mapper(componentModel = "spring")
public interface DeadliftStrengthMapper {
    DeadliftStrengthDto toDto(DeadliftStrength entity);
    DeadliftStrength toEntity(DeadliftStrengthDto dto);
}
