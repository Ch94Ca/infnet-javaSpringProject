package br.edu.infnet.CarlosAraujo.application.port.in;

import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserDataUpdateDTO;
import br.edu.infnet.CarlosAraujo.adapters.in.web.dto.UserResponseDTO;
import br.edu.infnet.CarlosAraujo.domain.enums.Role;

public interface AdminService {
    UserResponseDTO updateUserData(String email, UserDataUpdateDTO userDTO);
    void deleteUserByEmail(String email);
    UserResponseDTO changeUserRole(String email, Role newRole);
    void initializeAdminUser();
}