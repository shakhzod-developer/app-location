package sales.applocation.employee.domain;

import java.util.List;

public interface EmployeeRepository {

    void addEmployee(Employee employee);

    Employee findsEmployee(EmployeeId id);

    List<Employee> findAllEmployees();

    void removeEmployee(EmployeeId id);

    Object findsByUsername(String username);
}
