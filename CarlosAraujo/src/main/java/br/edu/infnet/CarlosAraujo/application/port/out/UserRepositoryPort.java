package br.edu.infnet.CarlosAraujo.application.port.out;

import br.edu.infnet.CarlosAraujo.domain.enums.Role;
import br.edu.infnet.CarlosAraujo.domain.user.User;

import java.util.Optional;

public interface UserRepositoryPort {
    User save(User user);
    Boolean existsByEmail(String email);
    Boolean existsByUserRole(Role role);
    Long deleteByEmail(String email);
    Optional<User> findByEmail(String email);
}
