package sales.applocation.employee.domain;

import java.util.UUID;

public record EmployeeId(UUID id) {
    public static EmployeeId newId() {
        return new EmployeeId(UUID.randomUUID());
    }
}
