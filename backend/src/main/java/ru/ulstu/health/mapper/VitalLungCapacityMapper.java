package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.VitalLungCapacityDto;
import ru.ulstu.health.entity.VitalLungCapacity;

@Mapper(componentModel = "spring")
public interface VitalLungCapacityMapper {
    VitalLungCapacityDto toDto(VitalLungCapacity entity);
    VitalLungCapacity toEntity(VitalLungCapacityDto dto);
}
