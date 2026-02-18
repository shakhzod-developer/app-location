package sales.applocation.tracking.domain;

import java.time.LocalDateTime;

public record LocationPoint(
        double lat,
        double lon,
        LocalDateTime date
) {
}
