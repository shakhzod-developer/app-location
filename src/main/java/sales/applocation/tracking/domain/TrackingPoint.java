package sales.applocation.tracking.domain;

import sales.applocation.orders.domain.OrderId;

import java.util.List;

public class TrackingPoint {

    private final OrderId orderId;

    private final List<LocationPoint> points;

    public TrackingPoint(OrderId orderId, List<LocationPoint> points) {
        this.orderId = orderId;
        this.points = points;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public List<LocationPoint> getPoints() {
        return points;
    }
}
