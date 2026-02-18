package sales.applocation.users.infrastructure.mapper;

import sales.applocation.shared.role.Role;
import sales.applocation.users.domain.User;
import sales.applocation.users.domain.UserId;
import sales.applocation.users.infrastructure.persistence.UserJpaEntity;

public class UserMapper {

    public static UserJpaEntity toJpa(User user) {
        if (user == null) return null;

        UserJpaEntity userJpaEntity = new UserJpaEntity();
        userJpaEntity.setId(user.getId().id());
        userJpaEntity.setUsername(user.getUsername());
        userJpaEntity.setPassword(user.getPassword());
        userJpaEntity.setPhone(user.getPhone());
        userJpaEntity.setRole(user.getRole().name());

        return userJpaEntity;
    }

    public static User toDomain(UserJpaEntity userJpaEntity) {

        if (userJpaEntity == null) return null;

        return new User.UserBuilder()
                .id(new UserId(userJpaEntity.getId()))
                .username(userJpaEntity.getUsername())
                .phone(userJpaEntity.getPhone())
                .password(userJpaEntity.getPassword())
                .role(Role.valueOf(userJpaEntity.getRole())).build();
    }
}
