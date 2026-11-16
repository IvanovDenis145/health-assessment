package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.HeartRateDto;
import ru.ulstu.health.entity.HeartRate;

@Mapper(componentModel = "spring")
public interface HeartRateMapper {
    HeartRateDto toDto(HeartRate entity);
    HeartRate toEntity(HeartRateDto dto);
}
