package sales.applocation.employee.infrastructure.mapper;

import sales.applocation.employee.domain.Employee;
import sales.applocation.employee.domain.EmployeeId;
import sales.applocation.employee.infrastructure.persistence.EmployeeJpaEntity;
import sales.applocation.shared.role.Role;

public class EmployeeMapper {


    public static EmployeeJpaEntity toJpa(Employee employee) {
        return new EmployeeJpaEntity(
                employee.getId().id(),
                employee.getUsername(),
                employee.getPassword(),
                employee.getPhone(),
                employee.getRole().name(),
                employee.isOnline()
        );
    }

    public static Employee toDomain(EmployeeJpaEntity entity) {
        Employee employee = Employee.create(
                new EmployeeId(entity.getId()),
                entity.getUsername(),
                entity.getPhone(),
                entity.getPassword()
        );

        if (entity.isOnline()) {
            employee.goOnline();
        } else {
            employee.goOffline();
        }

        return employee;
    }
}
