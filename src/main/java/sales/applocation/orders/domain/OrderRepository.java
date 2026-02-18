package sales.applocation.orders.domain;

import sales.applocation.employee.domain.EmployeeId;
import sales.applocation.users.domain.UserId;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository {

    void save(Order order);

    Order findById(OrderId orderId);

    List<Order> findByUserId(UserId userId);

    List<Order> findByEmployeeId(EmployeeId employeeId);

    List<Order> findAllByOrderStatus(OrderStatus status);

    List<Order> findByEmployeeIdAndDateRange(EmployeeId empId, LocalDateTime start, LocalDateTime end);
}
