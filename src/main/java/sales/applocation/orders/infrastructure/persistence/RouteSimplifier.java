package sales.applocation.orders.infrastructure.persistence;

import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;

public class RouteSimplifier {
    public static Geometry simplify(Geometry geometry, double distanceTolerance) {
        if (geometry == null) return null;
        return DouglasPeuckerSimplifier.simplify(geometry, distanceTolerance);
    }
}