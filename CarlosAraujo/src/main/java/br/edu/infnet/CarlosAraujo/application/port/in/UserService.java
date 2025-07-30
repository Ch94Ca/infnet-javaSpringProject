package br.edu.infnet.CarlosAraujo.application.port.in;

import br.edu.infnet.CarlosAraujo.application.useCase.UserCreateCommand;
import br.edu.infnet.CarlosAraujo.application.useCase.UserUpdateCommand;
import br.edu.infnet.CarlosAraujo.domain.user.User;

public interface UserService {
    User createUser(UserCreateCommand userCreateCommand);
    User updateUserData(String email, UserUpdateCommand userUpdateCommand);
}