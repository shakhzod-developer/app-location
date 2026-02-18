package sales.applocation.shared.auth.dto;

import java.util.UUID;

public record AuthResponse(
        String token,
        String role,
        UUID id,
        String type
) {
}
