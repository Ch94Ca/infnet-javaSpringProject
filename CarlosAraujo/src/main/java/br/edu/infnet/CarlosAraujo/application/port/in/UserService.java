package br.edu.infnet.CarlosAraujo.application.port.in;

import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserCreateDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserDataUpdateDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserResponseDTO;

public interface UserService {
    UserResponseDTO createUser(UserCreateDTO userDTO);
    UserResponseDTO updateUserData(String email, UserDataUpdateDTO userDTO);
}