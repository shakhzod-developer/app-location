package sales.applocation.orders.application.dto;

import java.util.UUID;

public record SwipeOnlineRequest(
        UUID employeeId
) {
}
