package ru.ulstu.health.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import ru.ulstu.health.dto.user.UserCreateDto;
import ru.ulstu.health.dto.user.UserDto;
import ru.ulstu.health.dto.user.UserUpdateDto;
import ru.ulstu.health.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(UserCreateDto dto);
    void updateEntityFromDto(UserUpdateDto dto, @MappingTarget User user);
}
