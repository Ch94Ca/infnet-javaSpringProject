package br.edu.infnet.carlos_araujo.application.port.in;

import br.edu.infnet.carlos_araujo.application.use_case.user.UserCreateCommand;
import br.edu.infnet.carlos_araujo.application.use_case.user.UserUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.User;

public interface UserService {
    User createUser(UserCreateCommand userCreateCommand);
    User updateUserData(String email, UserUpdateCommand userUpdateCommand);
    void changePassword(String email, String currentPassword, String newPassword);
}