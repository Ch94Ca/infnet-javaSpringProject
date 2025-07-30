package br.edu.infnet.CarlosAraujo.adapters.out.persistence.mapper;

import br.edu.infnet.CarlosAraujo.domain.user.User;
import br.edu.infnet.CarlosAraujo.adapters.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
@Component
public final class UserMapper {

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserEntity(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getPassword(),
                user.getUserRole(),
                user.isActive(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        return new User(
                entity.getId(),
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getUserRole(),
                entity.isActive(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
