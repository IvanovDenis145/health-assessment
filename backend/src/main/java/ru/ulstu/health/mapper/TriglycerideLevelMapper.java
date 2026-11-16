package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.TriglycerideLevelDto;
import ru.ulstu.health.entity.TriglycerideLevel;

@Mapper(componentModel = "spring")
public interface TriglycerideLevelMapper {
    TriglycerideLevelDto toDto(TriglycerideLevel entity);
    TriglycerideLevel toEntity(TriglycerideLevelDto dto);
}
