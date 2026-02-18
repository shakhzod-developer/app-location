package sales.applocation.orders.infrastructure.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderJpaEntity {

    @Id
    @Column(length = 36)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private UUID userId;

    private UUID employeeId;

    @Column(columnDefinition = "Json")
    private Point location;

    @Column(columnDefinition = "Json")
    private LineString finalRoute;

    private LocalDateTime orderAt;

    private LocalDateTime acceptedAt;

    private LocalDateTime deliveredAt;

    private String status;
}
