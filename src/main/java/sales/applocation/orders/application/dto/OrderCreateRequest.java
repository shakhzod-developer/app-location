package sales.applocation.orders.application.dto;

import java.util.UUID;

public record OrderCreateRequest(
        UUID id,
        double latitude,
        double longitude
) {
}
