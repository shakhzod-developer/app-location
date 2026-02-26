package sales.applocation.orders.domain;

import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import sales.applocation.employee.domain.EmployeeId;
import sales.applocation.users.domain.UserId;

import java.time.LocalDateTime;
import java.util.Objects;

public class Order {

    private final OrderId orderId;

    private final UserId userId;

    private EmployeeId employeeId;

    private final Point location;

    private LocalDateTime orderAt;

    private LocalDateTime acceptedAt;

    private LocalDateTime deliveredAt;

    private OrderStatus status;

    private LineString finalRoute;

    public Order(OrderId orderId, UserId userId, Point location) {
        this.orderId = Objects.requireNonNull(orderId, "Order ID is required");
        this.userId = Objects.requireNonNull(userId, "User ID is required");
        this.location = location;

        this.orderAt = LocalDateTime.now();
        this.status = OrderStatus.ORDERED;

        this.employeeId = null;
        this.acceptedAt = null;
        this.deliveredAt = null;
    }

    public Order(OrderBuilder builder){
        this.orderId = builder.orderId;
        this.userId = builder.userId;
        this.location = builder.location;
        this.orderAt = builder.orderAt;
        this.acceptedAt = builder.acceptedAt;
        this.deliveredAt = builder.deliveredAt;
        this.status = builder.status;
    }

    public LocalDateTime getAcceptedAt() {
        return acceptedAt;
    }

    public LocalDateTime getDeliveredAt() {
        return deliveredAt;
    }

    public Point getLocation() {
        return location;
    }

    public LocalDateTime getOrderAt() {
        return orderAt;
    }

    public OrderId getOrderId() {
        return orderId;
    }

    public EmployeeId getEmployeeId() {
        return employeeId;
    }

    public UserId getUserId() {
        return userId;
    }

    public OrderStatus getStatus(){
        return status;
    }


    public void accept(EmployeeId employeeId) {
        if (this.status != OrderStatus.ORDERED) {
            throw new IllegalStateException("Only ORDERED items can be accepted.");
        }
        if (this.employeeId != null) {
            throw new IllegalStateException("This order is already claimed by another employee.");
        }

        this.employeeId = Objects.requireNonNull(employeeId);
        this.acceptedAt = LocalDateTime.now();
        this.status = OrderStatus.ACCEPTED;
    }

    public void reject() {
        if (this.status == OrderStatus.DELIVERED) {
            throw new IllegalStateException("Cannot reject an order that is already delivered.");
        }
        this.status = OrderStatus.REJECTED;
    }

    public void markAsDelivered(LineString route) {
        if (this.status != OrderStatus.ACCEPTED) {
            throw new IllegalStateException("Order must be ACCEPTED first.");
        }
        this.finalRoute = route; // Store map history
        this.status = OrderStatus.DELIVERED;
        this.deliveredAt = java.time.LocalDateTime.now();
    }

    public LineString getFinalRoute() {
        return finalRoute;
    }

    public void backUpToOrdered() {
        this.status = OrderStatus.ORDERED;
    }


    public static class OrderBuilder {
        private OrderId orderId;
        private UserId userId;
        private EmployeeId employeeId;
        private Point location;
        private OrderStatus status;
        private LineString finalRoute;
        private LocalDateTime orderAt;
        private LocalDateTime acceptedAt;
        private LocalDateTime deliveredAt;


        public OrderBuilder orderId(OrderId orderId) {
            this.orderId = orderId;
            return this;
        }

        public OrderBuilder userId(UserId userId) {
            this.userId = userId;
            return this;
        }

        public OrderBuilder employeeId(EmployeeId employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public OrderBuilder location(Point location) {
            this.location = location;
            return this;
        }

        public OrderBuilder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public OrderBuilder finalRoute(LineString finalRoute) {
            this.finalRoute = finalRoute;
            return this;
        }

        public OrderBuilder orderAt(LocalDateTime orderAt) {
            this.orderAt = orderAt;
            return this;
        }

        public OrderBuilder acceptedAt(LocalDateTime acceptedAt) {
            this.acceptedAt = acceptedAt;
            return this;
        }

        public OrderBuilder deliveredAt(LocalDateTime deliveredAt) {
            this.deliveredAt = deliveredAt;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }
}
