package sales.applocation.tracking.domain;

import sales.applocation.orders.domain.OrderId;
import sales.applocation.tracking.application.dto.AdminMapOfEmployeeDto;

import java.util.List;

public interface TrackingRepository {

    void save(TrackingPoint trackingPoint);

    void delete(OrderId id);

    List<AdminMapOfEmployeeDto> findAllActiveLocations();

    TrackingPoint findByOrderId(OrderId id); // Used for completion

}
