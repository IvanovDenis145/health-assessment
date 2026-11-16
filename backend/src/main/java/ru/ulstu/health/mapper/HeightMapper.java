package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.HeightDto;
import ru.ulstu.health.entity.Height;

@Mapper(componentModel = "spring")
public interface HeightMapper {
    HeightDto toDto(Height height);
    Height toEntity(HeightDto dto);
}
