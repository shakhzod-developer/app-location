package sales.applocation.users.application.dto;

import sales.applocation.users.domain.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String username,
        String phone
) {
    public static UserResponse fromDomain(User user) {
        return new UserResponse(
                user.getId().id(),
                user.getUsername(),
                user.getPhone()
        );
    }
}
