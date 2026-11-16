package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.RespiratoryRateDto;
import ru.ulstu.health.entity.RespiratoryRate;

@Mapper(componentModel = "spring")
public interface RespiratoryRateMapper {
    RespiratoryRateDto toDto(RespiratoryRate entity);
    RespiratoryRate toEntity(RespiratoryRateDto dto);
}
