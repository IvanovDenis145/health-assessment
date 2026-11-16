package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.PhagocyticIndexDto;
import ru.ulstu.health.entity.PhagocyticIndex;

@Mapper(componentModel = "spring")
public interface PhagocyticIndexMapper {
    PhagocyticIndexDto toDto(PhagocyticIndex entity);
    PhagocyticIndex toEntity(PhagocyticIndexDto dto);
}
