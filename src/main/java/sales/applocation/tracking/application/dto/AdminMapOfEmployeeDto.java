package sales.applocation.tracking.application.dto;

public record AdminMapOfEmployeeDto(
        String orderId,
        String employeeName,
        double lat,
        double lon
) {
}
