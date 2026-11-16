package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.DynamometryDto;
import ru.ulstu.health.entity.Dynamometry;

@Mapper(componentModel = "spring")
public interface DynamometryMapper {
    DynamometryDto toDto(Dynamometry entity);
    Dynamometry toEntity(DynamometryDto dto);
}
