package sales.applocation.users.domain;

import java.util.UUID;

public record UserId(UUID id) {

    public static UserId value(){
        return new UserId(UUID.randomUUID());
    }
}
