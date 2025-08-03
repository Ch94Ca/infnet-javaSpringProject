package br.edu.infnet.carlos_araujo.application.port.in;

import br.edu.infnet.carlos_araujo.application.use_case.UserCreateCommand;
import br.edu.infnet.carlos_araujo.application.use_case.UserUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.user.User;

public interface UserService {
    User createUser(UserCreateCommand userCreateCommand);
    User updateUserData(String email, UserUpdateCommand userUpdateCommand);
    void changePassword(String email, String currentPassword, String newPassword);
}