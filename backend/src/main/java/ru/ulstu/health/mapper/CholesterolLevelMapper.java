package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.CholesterolLevelDto;
import ru.ulstu.health.entity.CholesterolLevel;

@Mapper(componentModel = "spring")
public interface CholesterolLevelMapper {
    CholesterolLevelDto toDto(CholesterolLevel entity);
    CholesterolLevel toEntity(CholesterolLevelDto dto);
}
