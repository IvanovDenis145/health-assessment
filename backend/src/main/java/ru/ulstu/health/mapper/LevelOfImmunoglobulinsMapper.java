package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.LevelOfImmunoglobulinsDto;
import ru.ulstu.health.entity.LevelOfImmunoglobulins;

@Mapper(componentModel = "spring")
public interface LevelOfImmunoglobulinsMapper {
    LevelOfImmunoglobulinsDto toDto(LevelOfImmunoglobulins entity);
    LevelOfImmunoglobulins toEntity(LevelOfImmunoglobulinsDto dto);
}
