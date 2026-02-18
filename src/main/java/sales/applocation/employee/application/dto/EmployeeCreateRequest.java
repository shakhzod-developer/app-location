package sales.applocation.employee.application.dto;

public record EmployeeCreateRequest(
        String username,
        String phone,
        String password
) {
}
