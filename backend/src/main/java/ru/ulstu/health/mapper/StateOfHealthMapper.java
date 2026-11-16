package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.StateOfHealthDto;
import ru.ulstu.health.entity.StateOfHealth;

@Mapper(componentModel = "spring")
public interface StateOfHealthMapper {
    StateOfHealthDto toDto(StateOfHealth entity);
    StateOfHealth toEntity(StateOfHealthDto dto);
}
