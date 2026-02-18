package sales.applocation.orders.application.dto;

import sales.applocation.tracking.domain.LocationPoint;

import java.util.UUID;

public record AcceptsOrderRequest(
        UUID employeeId,
        LocationPoint point
) {
}
