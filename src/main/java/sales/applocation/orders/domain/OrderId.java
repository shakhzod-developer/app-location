package sales.applocation.orders.domain;

import java.util.UUID;

public record OrderId(
        UUID id
) {
    public static OrderId value(){
        return new OrderId(UUID.randomUUID());
    }
}
