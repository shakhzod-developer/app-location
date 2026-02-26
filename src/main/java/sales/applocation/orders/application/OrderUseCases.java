package sales.applocation.orders.application;

import org.locationtech.jts.geom.Point;
import sales.applocation.employee.domain.EmployeeId;
import sales.applocation.orders.domain.Order;
import sales.applocation.orders.domain.OrderId;
import sales.applocation.orders.domain.OrderRepository;
import sales.applocation.users.domain.UserId;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class OrderUseCases {

    private final OrderRepository orderRepository;

    public OrderUseCases(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public void createOrder(UserId useId, Point location) {
        Order order = new Order(OrderId.value(), useId, location);
        orderRepository.save(order);
    }

    public List<Order> getOrdersByUserId(UserId userId) {
        return orderRepository.findByUserId(userId);
    }

    public List<Order> getOrdersByEmployeeId(EmployeeId employeeId) {
        return orderRepository.findByEmployeeId(employeeId);
    }//TODO this one brings the order by employeeId and DATE


    public List<Order> getEmployeeDailyHistory(EmployeeId empId, LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        return orderRepository.findByEmployeeIdAndDateRange(empId, start, end);
    }
}
