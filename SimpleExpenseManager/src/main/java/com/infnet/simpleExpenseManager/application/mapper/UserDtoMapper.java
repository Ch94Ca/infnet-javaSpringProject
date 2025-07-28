package com.infnet.simpleExpenseManager.application.mapper;

import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserCreateDTO;
import com.infnet.simpleExpenseManager.adapters.in.web.dto.UserResponseDTO;
import com.infnet.simpleExpenseManager.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "userRole", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    User toDomain(UserCreateDTO dto);

    @Mapping(source = "userRole", target = "role")
    UserResponseDTO toResponseDto(User user);
}
