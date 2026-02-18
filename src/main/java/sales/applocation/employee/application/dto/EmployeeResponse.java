package sales.applocation.employee.application.dto;

import sales.applocation.employee.domain.Employee;

import java.util.UUID;

public record EmployeeResponse(
        UUID id,
        String username,
        String phone,
        String role
) {

    public static EmployeeResponse toDomain(Employee employee) {
        return new EmployeeResponse(
                employee.getId().id(),
                employee.getUsername(),
                employee.getPhone(),
                employee.getRole().name()
        );
    }
}
