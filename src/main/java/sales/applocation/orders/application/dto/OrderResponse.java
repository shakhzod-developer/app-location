package sales.applocation.orders.application.dto;

import sales.applocation.orders.domain.Order;

import java.util.UUID;

public record OrderResponse(
        UUID orderId,
        UUID userId,
        UUID employeeId,
        String status
) {

    public static OrderResponse fromDomain(Order order) {
        return new OrderResponse(
                order.getOrderId().id(),
                order.getUserId().id(),
                order.getEmployeeId() != null ? order.getEmployeeId().id() : null,
                order.getStatus().name()
        );
    }
}
