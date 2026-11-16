package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.ThicknessOfTheSkinFatFoldDto;
import ru.ulstu.health.entity.ThicknessOfTheSkinFatFold;

@Mapper(componentModel = "spring")
public interface ThicknessOfTheSkinFatFoldMapper {
    ThicknessOfTheSkinFatFoldDto toDto(ThicknessOfTheSkinFatFold entity);
    ThicknessOfTheSkinFatFold toEntity(ThicknessOfTheSkinFatFoldDto dto);
}
