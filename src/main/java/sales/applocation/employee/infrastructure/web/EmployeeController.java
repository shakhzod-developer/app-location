package sales.applocation.employee.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sales.applocation.employee.application.EmployeeUseCases;
import sales.applocation.employee.application.dto.EmployeeCreateRequest;
import sales.applocation.employee.application.dto.EmployeeResponse;
import sales.applocation.employee.domain.EmployeeId;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeUseCases employeeUseCases;

    @PostMapping("/createEmployee")
    public ResponseEntity<Void> createEmployee(@RequestBody EmployeeCreateRequest request) {
        employeeUseCases.createEmployee(
                request.username(),
                request.phone(),
                request.password()
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/getEmployee/{id}")
    public ResponseEntity<EmployeeResponse> getEmployee(@PathVariable UUID id) {
        var employee = employeeUseCases.getEmployee(new EmployeeId(id));
        return ResponseEntity.ok(EmployeeResponse.toDomain(employee));
    }

    @GetMapping("/getAllEmployee")
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> employees = employeeUseCases.getAllEmployees()
                .stream()
                .map(EmployeeResponse::toDomain)
                .collect(Collectors.toList());
        return ResponseEntity.ok(employees);
    }

    @DeleteMapping("/deleteEmployee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable UUID id) {
        employeeUseCases.deleteEmployee(new EmployeeId(id));
        return ResponseEntity.noContent().build();
    }

}
