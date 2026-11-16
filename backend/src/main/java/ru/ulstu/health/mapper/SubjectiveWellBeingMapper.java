package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import ru.ulstu.health.dto.indicator.SubjectiveWellBeingDto;
import ru.ulstu.health.entity.SubjectiveWellBeing;

@Mapper(componentModel = "spring")
public interface SubjectiveWellBeingMapper {
    SubjectiveWellBeingDto toDto(SubjectiveWellBeing entity);
    SubjectiveWellBeing toEntity(SubjectiveWellBeingDto dto);
}
