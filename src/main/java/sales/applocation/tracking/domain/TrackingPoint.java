package sales.applocation.tracking.domain;

import sales.applocation.employee.domain.EmployeeId;
import sales.applocation.orders.domain.OrderId;
import sales.applocation.users.domain.User;

import java.util.List;

public class TrackingPoint {

    private final OrderId orderId;

    private final EmployeeId employeeId;

    private final List<LocationPoint> points;

    public TrackingPoint(OrderId orderId, EmployeeId employeeId, List<LocationPoint> points) {
        this.orderId = orderId;
        this.employeeId = employeeId;
        this.points = points;
    }


    public OrderId getOrderId() {
        return orderId;
    }

    public List<LocationPoint> getPoints() {
        return points;
    }

    public LocationPoint getLatest() {
        return points.get(points.size() - 1);
    }

    public EmployeeId getEmployeeId() {
        return employeeId;
    }
}
