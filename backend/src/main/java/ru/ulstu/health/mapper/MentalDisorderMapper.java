package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.MentalDisorderDto;
import ru.ulstu.health.entity.MentalDisorder;

@Mapper(componentModel = "spring")
public interface MentalDisorderMapper {
    MentalDisorderDto toDto(MentalDisorder entity);
    MentalDisorder toEntity(MentalDisorderDto dto);
}
