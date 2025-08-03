package br.edu.infnet.carlos_araujo.application.port.in;

import br.edu.infnet.carlos_araujo.application.use_case.user.UserUpdateCommand;
import br.edu.infnet.carlos_araujo.domain.enums.Role;
import br.edu.infnet.carlos_araujo.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminService {
    User updateUserData(String email, UserUpdateCommand userUpdateCommand);
    void deleteUserByEmail(String email);
    User changeUserRole(String email, Role newRole);
    User changeUserActiveStatus(String email, boolean newActiveStatus);
    public Page<User> getUsers(Pageable pageable);
    void initializeAdminUser();
}