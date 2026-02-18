package sales.applocation.employee.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import sales.applocation.employee.application.dto.EmployeeCreateRequest;
import sales.applocation.employee.domain.Employee;
import sales.applocation.employee.domain.EmployeeId;
import sales.applocation.employee.domain.EmployeeRepository;

import java.util.UUID;

public class RegisterEmployeeUseCase {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterEmployeeUseCase(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerEmployee(EmployeeCreateRequest request) {

        if ((boolean) employeeRepository.findsByUsername(request.username())){
            throw new RuntimeException("Employee already exists");
        }

        Employee employee = Employee.create(
                new EmployeeId(UUID.randomUUID()),
                request.username(),
                request.phone(),
                passwordEncoder.encode(request.password())
        );

        employeeRepository.addEmployee(employee);
    }
}
