package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.StressLevelDto;
import ru.ulstu.health.entity.StressLevel;

@Mapper(componentModel = "spring")
public interface StressLevelMapper {
    StressLevelDto toDto(StressLevel entity);
    StressLevel toEntity(StressLevelDto dto);
}
