package sales.applocation.orders.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sales.applocation.employee.domain.EmployeeRepository;
import sales.applocation.orders.application.AcceptOrderUseCase;
import sales.applocation.orders.application.OrderUseCases;
import sales.applocation.orders.domain.OrderRepository;
import sales.applocation.orders.infrastructure.mapper.OrderMapper;
import sales.applocation.orders.infrastructure.persistence.OrderJpaRepository;
import sales.applocation.orders.infrastructure.persistence.OrderRepositoryImpl;
import sales.applocation.tracking.domain.TrackingRepository;

@Configuration
public class OrderConfig {

    @Bean
    public OrderRepository orderRepository(OrderJpaRepository orderJpaRepository, OrderMapper orderMapper) {
        return new OrderRepositoryImpl(orderJpaRepository, orderMapper);
    }

    @Bean
    public OrderUseCases orderUseCases(OrderRepository orderRepository) {
        return new OrderUseCases(orderRepository);
    }

    @Bean
    public AcceptOrderUseCase acceptOrderUseCase(
            TrackingRepository trackingRepository,
            OrderRepository orderRepository,
            EmployeeRepository employeeRepository) {
        return new AcceptOrderUseCase(trackingRepository,orderRepository, employeeRepository);
    }


}
