package sales.applocation.employee.application;

import sales.applocation.employee.domain.*;


import java.util.List;

public class EmployeeUseCases {

    private final EmployeeRepository employeeRepository;

    public EmployeeUseCases(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void createEmployee(String username, String phone, String password) {
        Employee employee = Employee.create(
                EmployeeId.newId(),
                username,
                phone,
                password
        );

        employeeRepository.addEmployee(employee);
    }

    public Employee getEmployee(EmployeeId id) {
        return employeeRepository.findsEmployee(id);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAllEmployees();
    }

    public void deleteEmployee(EmployeeId id) {
        employeeRepository.removeEmployee(id);
    }
}
