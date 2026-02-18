package sales.applocation.tracking.domain;

import sales.applocation.orders.domain.OrderId;
import sales.applocation.tracking.application.dto.AdminMapOfEmployeeDto;

import java.util.List;

public interface TrackingRepository {

    void save(TrackingPoint trackingPoint);

    void delete(OrderId id);

    TrackingPoint findByOrderId(OrderId oId);

    List<AdminMapOfEmployeeDto> findAllActiveLocations();
}
