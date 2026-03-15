package sales.applocation.orders.application;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sales.applocation.orders.domain.Order;
import sales.applocation.orders.domain.OrderRepository;
import sales.applocation.orders.domain.OrderStatus;
import sales.applocation.tracking.domain.TrackingRepository;

import java.util.List;

@Service
@EnableScheduling
public class StaleOrderCleanupService {

    private final OrderRepository orderRepository;
    private final TrackingRepository trackingRepository;

    public StaleOrderCleanupService(OrderRepository orderRepo, TrackingRepository trackingRepo) {
        this.orderRepository = orderRepo;
        this.trackingRepository = trackingRepo;
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void revertStaleOrders() {
        List<Order> activeOrders = orderRepository.findAllByOrderStatus(OrderStatus.ACCEPTED);

        for (Order order : activeOrders) {
            if (trackingRepository.findByOrderId(order.getOrderId()) == null) {
                order.backUpToOrdered();
                orderRepository.save(order);
            }
        }
    }
}