package br.edu.infnet.carlos_araujo.application.port.in;

import br.edu.infnet.carlos_araujo.application.use_case.UserUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.enums.Role;
import br.edu.infnet.carlos_araujo.domain.user.User;

public interface AdminService {
    User updateUserData(String email, UserUpdateCommand userUpdateCommand);
    void deleteUserByEmail(String email);
    User changeUserRole(String email, Role newRole);
    void initializeAdminUser();
}