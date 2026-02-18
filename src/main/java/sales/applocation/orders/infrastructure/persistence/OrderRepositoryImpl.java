package sales.applocation.orders.infrastructure.persistence;

import org.springframework.stereotype.Repository;
import sales.applocation.employee.domain.EmployeeId;
import sales.applocation.orders.domain.Order;
import sales.applocation.orders.domain.OrderId;
import sales.applocation.orders.domain.OrderRepository;
import sales.applocation.orders.domain.OrderStatus;
import sales.applocation.orders.infrastructure.mapper.OrderMapper;
import sales.applocation.users.domain.UserId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderMapper orderMapper;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository, OrderMapper orderMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    public void save(Order order) {
        orderJpaRepository.save(orderMapper.toJpa(order));
    }

    @Override
    public List<Order> findByEmployeeId(EmployeeId employeeId) {
        return orderJpaRepository.findByEmployeeId(employeeId.id()).stream()
                .map(OrderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Order findById(OrderId orderId) {
        return orderJpaRepository.findById(orderId.id())
                .map(OrderMapper::toDomain)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    @Override
    public List<Order> findByUserId(UserId userId) {
        return orderJpaRepository.findByUserId(userId.id()).stream()
                .map(OrderMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findAllByOrderStatus(OrderStatus status) {
        return orderJpaRepository.findAllByStatus(String.valueOf(status));
    }


    @Override
    public List<Order> findByEmployeeIdAndDateRange(EmployeeId empId, LocalDateTime start, LocalDateTime end) {
        return orderJpaRepository.findByEmployeeIdAndDeliveredAtBetween(empId.id(), start, end).
                stream()
                .map(OrderMapper::toDomain)
                .collect(Collectors.toList());
    }
}
