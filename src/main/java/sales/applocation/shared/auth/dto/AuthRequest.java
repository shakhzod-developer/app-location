package sales.applocation.shared.auth.dto;

public record AuthRequest(
        String username,
        String password
) {
}
