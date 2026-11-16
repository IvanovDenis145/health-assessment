package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.WeightDto;
import ru.ulstu.health.entity.Weight;

@Mapper(componentModel = "spring")
public interface WeightMapper {
    WeightDto toDto(Weight entity);
    Weight toEntity(WeightDto dto);
}
