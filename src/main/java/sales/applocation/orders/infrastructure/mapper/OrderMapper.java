package sales.applocation.orders.infrastructure.mapper;

import org.springframework.stereotype.Component;
import sales.applocation.employee.domain.EmployeeId;
import sales.applocation.orders.domain.Order;
import sales.applocation.orders.domain.OrderId;
import sales.applocation.orders.domain.OrderStatus;
import sales.applocation.orders.infrastructure.persistence.OrderJpaEntity;
import sales.applocation.users.domain.UserId;

@Component
public class OrderMapper {

    public OrderJpaEntity toJpa(Order domain) {
        if (domain == null) return null;

        OrderJpaEntity entity = new OrderJpaEntity();

        entity.setId(domain.getOrderId().id());
        entity.setUserId(domain.getUserId().id());

        if (domain.getEmployeeId() != null) {
            entity.setEmployeeId(domain.getEmployeeId().id());
        }

        entity.setLocation(domain.getLocation());
        entity.setFinalRoute(domain.getFinalRoute());

        entity.setStatus(domain.getStatus().name());

        entity.setOrderAt(domain.getOrderAt());
        entity.setAcceptedAt(domain.getAcceptedAt());
        entity.setDeliveredAt(domain.getDeliveredAt());

        return entity;
    }


    public static Order toDomain(OrderJpaEntity entity) {
        if (entity == null) return null;

        return new Order.OrderBuilder()
                .orderId(new OrderId(entity.getId()))
                .userId(new UserId(entity.getUserId()))
                .employeeId(entity.getEmployeeId() != null ? new EmployeeId(entity.getEmployeeId()) : null)
                .location(entity.getLocation())
                .status(OrderStatus.valueOf(entity.getStatus()))
                .finalRoute(entity.getFinalRoute())
                .orderAt(entity.getOrderAt())
                .acceptedAt(entity.getAcceptedAt())
                .deliveredAt(entity.getDeliveredAt())
                .build();
    }
}