package sales.applocation.employee.domain;

import sales.applocation.employee.infrastructure.persistence.EmployeeJpaEntity;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {

    void addEmployee(Employee employee);

    Employee findsEmployee(EmployeeId id);

    List<Employee> findAllEmployees();

    void removeEmployee(EmployeeId id);

    boolean existsByUsername(String username);

    Optional<EmployeeJpaEntity> findsByUsername(String username);
}
