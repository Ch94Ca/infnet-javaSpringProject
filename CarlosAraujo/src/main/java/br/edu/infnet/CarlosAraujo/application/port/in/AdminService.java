package br.edu.infnet.CarlosAraujo.application.port.in;

import br.edu.infnet.CarlosAraujo.application.useCase.UserUpdateCommand;
import br.edu.infnet.CarlosAraujo.domain.enums.Role;
import br.edu.infnet.CarlosAraujo.domain.user.User;

public interface AdminService {
    User updateUserData(String email, UserUpdateCommand userUpdateCommand);
    void deleteUserByEmail(String email);
    User changeUserRole(String email, Role newRole);
    void initializeAdminUser();
}