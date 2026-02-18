package sales.applocation.users.application.dto;

public record UserCreateRequest(
        String username,
        String password,
        String phone
) {
}
