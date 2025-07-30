package br.edu.infnet.CarlosAraujo.adapters.in.web.mapper;

import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserCreateDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserDataUpdateDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserResponseDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.auth.LoginRequestDTO;
import br.edu.infnet.CarlosAraujo.application.useCase.LoginCommand;
import br.edu.infnet.CarlosAraujo.application.useCase.UserCreateCommand;
import br.edu.infnet.CarlosAraujo.application.useCase.UserUpdateCommand;
import br.edu.infnet.CarlosAraujo.domain.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    @Mapping(source = "userRole", target = "role")
    UserResponseDTO toResponseDto(User user);

    UserUpdateCommand toUserUpdateCommand(UserDataUpdateDTO dto);
    UserCreateCommand toUserCreateCommand(UserCreateDTO dto);
    LoginCommand toLoginCommand(LoginRequestDTO dto);
}
