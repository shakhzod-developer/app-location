package sales.applocation.orders.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sales.applocation.employee.domain.EmployeeId;
import sales.applocation.orders.application.AcceptOrderUseCase;
import sales.applocation.orders.application.OrderUseCases;
import sales.applocation.orders.application.dto.AcceptsOrderRequest;
import sales.applocation.orders.application.dto.OrderCreateRequest;
import sales.applocation.orders.application.dto.OrderResponse;
import sales.applocation.orders.application.dto.SwipeOnlineRequest;
import sales.applocation.orders.domain.OrderId;
import sales.applocation.tracking.domain.LocationPoint;
import sales.applocation.users.domain.UserId;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderUseCases orderUseCases;
    private final AcceptOrderUseCase acceptOrderUseCase;
    private final GeometryFactory geometryFactory =
            new GeometryFactory(new PrecisionModel(), 4326);


    @PostMapping("/createOrder")
    public ResponseEntity<Void> createOrder(@RequestBody OrderCreateRequest request) {
        Point location = geometryFactory.createPoint(new Coordinate(request.longitude(), request.latitude()));

        orderUseCases.createOrder(new UserId(request.id()), location);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/swipe-online")
    public ResponseEntity<List<OrderResponse>> swipeOnline(@RequestBody SwipeOnlineRequest request) {
        var orders = acceptOrderUseCase.swipeOnline(
                new EmployeeId(request.employeeId())
        );

        var response = orders.stream().map(OrderResponse::fromDomain).toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/accept/{orderId}")
    public ResponseEntity<Void> acceptOrder(
            @PathVariable UUID orderId,
            @RequestBody AcceptsOrderRequest request) {

        acceptOrderUseCase.acceptOrder(new OrderId(orderId), new EmployeeId(request.employeeId()),request.point());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/addingPoint")
    public ResponseEntity<Void> addingPoint(@RequestBody OrderId oId,
                                            EmployeeId eId,
                                            List<LocationPoint> point) {
        acceptOrderUseCase.processTrackingBatch(oId, eId, point);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/complete/{orderId}")
    public ResponseEntity<Void> completeDelivery(@PathVariable UUID orderId) {
        acceptOrderUseCase.completeDelivery(new OrderId(orderId));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderResponse>> getByUserId(@PathVariable UUID userId) {
        var orders = orderUseCases.getOrdersByUserId(new UserId(userId));
        return ResponseEntity.ok(orders.stream().map(OrderResponse::fromDomain).toList());
    }
}
