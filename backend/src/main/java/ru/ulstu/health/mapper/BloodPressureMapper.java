package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.BloodPressureDto;
import ru.ulstu.health.entity.BloodPressure;

@Mapper(componentModel = "spring")
public interface BloodPressureMapper {
    BloodPressureDto toDto(BloodPressure entity);
    BloodPressure toEntity(BloodPressureDto dto);
}
