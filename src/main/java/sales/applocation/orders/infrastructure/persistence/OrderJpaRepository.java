package sales.applocation.orders.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import sales.applocation.orders.domain.Order;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderJpaRepository  extends JpaRepository<OrderJpaEntity, UUID> {

    List<OrderJpaEntity> findByEmployeeId(UUID employeeId);

    List<OrderJpaEntity> findByUserId(UUID userId);

    List<Order> findAllByStatus(String status);

    List<OrderJpaEntity> findByEmployeeIdAndDeliveredAtBetween(
            UUID employeeId,
            LocalDateTime start,
            LocalDateTime end
    );
}

