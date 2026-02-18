package sales.applocation.tracking.application.dto;

import sales.applocation.orders.domain.OrderId;
import sales.applocation.tracking.domain.LocationPoint;

import java.util.List;
import java.util.UUID;

public record SavingTrackDto(
        OrderId id,
        List<LocationPoint> points
) {
}
