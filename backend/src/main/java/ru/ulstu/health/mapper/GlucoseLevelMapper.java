package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.GlucoseLevelDto;
import ru.ulstu.health.entity.GlucoseLevel;

@Mapper(componentModel = "spring")
public interface GlucoseLevelMapper {
    GlucoseLevelDto toDto(GlucoseLevel entity);
    GlucoseLevel toEntity(GlucoseLevelDto dto);
}
