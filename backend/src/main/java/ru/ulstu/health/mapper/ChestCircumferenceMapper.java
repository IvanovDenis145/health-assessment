package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.ChestCircumferenceDto;
import ru.ulstu.health.entity.ChestCircumference;

@Mapper(componentModel = "spring")
public interface ChestCircumferenceMapper {
    ChestCircumferenceDto toDto(ChestCircumference entity);
    ChestCircumference toEntity(ChestCircumferenceDto dto);
}
