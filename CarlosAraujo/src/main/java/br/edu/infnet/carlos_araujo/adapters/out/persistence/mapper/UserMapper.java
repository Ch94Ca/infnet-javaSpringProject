package br.edu.infnet.carlos_araujo.adapters.out.persistence.mapper;

import br.edu.infnet.carlos_araujo.domain.User;
import br.edu.infnet.carlos_araujo.adapters.out.persistence.entity.UserEntity;
import org.springframework.stereotype.Component;
import java.util.ArrayList;

@Component
public final class UserMapper {

    public UserEntity toEntity(User user) {
        if (user == null) {
            return null;
        }

        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setName(user.getName());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setUserRole(user.getUserRole());
        entity.setActive(user.isActive());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        entity.setCategories(new ArrayList<>());

        return entity;
    }

    public User toDomain(UserEntity entity) {
        if (entity == null) {
            return null;
        }

        User user = new User();
        user.setId(entity.getId());
        user.setName(entity.getName());
        user.setEmail(entity.getEmail());
        user.setPassword(entity.getPassword());
        user.setUserRole(entity.getUserRole());
        user.setActive(entity.isActive());
        user.setCreatedAt(entity.getCreatedAt());
        user.setUpdatedAt(entity.getUpdatedAt());

        return user;
    }
}
