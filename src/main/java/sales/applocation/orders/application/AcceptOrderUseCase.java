package sales.applocation.orders.application;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.PrecisionModel;
import sales.applocation.employee.domain.Employee;
import sales.applocation.employee.domain.EmployeeId;
import sales.applocation.employee.domain.EmployeeRepository;
import sales.applocation.orders.domain.Order;
import sales.applocation.orders.domain.OrderId;
import sales.applocation.orders.domain.OrderRepository;
import sales.applocation.orders.domain.OrderStatus;
import sales.applocation.orders.infrastructure.persistence.RouteSimplifier;
import sales.applocation.tracking.application.TrackingBroadcastService;
import sales.applocation.tracking.domain.LocationPoint;

import sales.applocation.tracking.domain.TrackingPoint;
import sales.applocation.tracking.domain.TrackingRepository;

import java.util.List;

public class AcceptOrderUseCase {

    private final OrderRepository orderRepository;

    private final EmployeeRepository employeeRepository;

    private final TrackingRepository trackingRepository;

    private final TrackingBroadcastService trackingBroadcastService;

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);

    public AcceptOrderUseCase(
            TrackingRepository trackingRepository,
            OrderRepository orderRepository,
            EmployeeRepository employeeRepository,
            TrackingBroadcastService trackingBroadcastService) {
        this.trackingRepository = trackingRepository;
        this.orderRepository = orderRepository;
        this.employeeRepository = employeeRepository;
        this.trackingBroadcastService = trackingBroadcastService;
    }

    public List<Order> swipeOnline(EmployeeId empId) {
        Employee emp = employeeRepository.findsEmployee(empId);
        emp.goOnline();
        employeeRepository.addEmployee(emp);
        return orderRepository.findAllByOrderStatus(OrderStatus.ORDERED);
    }

    public void acceptOrder(OrderId oId, EmployeeId eId, LocationPoint location) {
        Order order = orderRepository.findById(oId);
        order.accept(eId);
        orderRepository.save(order);

        trackingRepository.save(new TrackingPoint(oId,eId, List.of(location)));
    }

    public void completeDelivery(OrderId oId) {
        TrackingPoint finalPath = trackingRepository.findByOrderId(oId);
        if (finalPath == null || finalPath.getPoints().isEmpty()) {
            throw new RuntimeException("No tracking data found for order: " + oId);
        }

        Coordinate[] coordinates = finalPath.getPoints().stream()
                .map(p -> new Coordinate(p.lon(), p.lat()))
                .toArray(Coordinate[]::new);
        LineString route = geometryFactory.createLineString(coordinates);
        LineString cleanRoute = (LineString) RouteSimplifier.simplify(route, 0.00005);

        Order order = orderRepository.findById(oId);
        order.markAsDelivered(cleanRoute);
        orderRepository.save(order);
        trackingRepository.delete(oId);
    }

    public void processTrackingBatch(OrderId oId, EmployeeId eId, List<LocationPoint> batch) {
        trackingRepository.save(new TrackingPoint(oId, eId, batch));
        trackingBroadcastService.broadcastOnlineEmployees();
    }
}
