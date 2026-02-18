package sales.applocation.tracking.application;

import sales.applocation.orders.domain.OrderId;
import sales.applocation.tracking.domain.LocationPoint;
import sales.applocation.tracking.domain.TrackingPoint;
import sales.applocation.tracking.domain.TrackingRepository;

import java.util.List;

public class TrackingUseCase {

    private final TrackingRepository trackingRepository;

    public TrackingUseCase(TrackingRepository trackingRepository) {
        this.trackingRepository = trackingRepository;
    }

    public void saveTrackPoints(OrderId id, List<LocationPoint> locations) {
        TrackingPoint trackingPoint = new TrackingPoint(id, locations);
        trackingRepository.save(trackingPoint);
    }
}
