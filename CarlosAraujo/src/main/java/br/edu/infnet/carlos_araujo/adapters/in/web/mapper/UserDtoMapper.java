package br.edu.infnet.carlos_araujo.adapters.in.web.mapper;

import br.edu.infnet.carlos_araujo.adapters.in.web.dto.user.UserCreateDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.dto.user.UserDataUpdateDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.dto.user.UserResponseDTO;
import br.edu.infnet.carlos_araujo.adapters.in.web.dto.auth.LoginRequestDTO;
import br.edu.infnet.carlos_araujo.application.use_case.auth.LoginCommand;
import br.edu.infnet.carlos_araujo.application.use_case.user.UserCreateCommand;
import br.edu.infnet.carlos_araujo.application.use_case.user.UserUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.User;
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
