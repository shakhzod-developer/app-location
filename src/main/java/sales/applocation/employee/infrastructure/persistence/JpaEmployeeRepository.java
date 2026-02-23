package sales.applocation.employee.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import sales.applocation.employee.domain.Employee;
import sales.applocation.employee.domain.EmployeeId;
import sales.applocation.employee.domain.EmployeeRepository;
import sales.applocation.employee.infrastructure.mapper.EmployeeMapper;

import java.util.List;

@RequiredArgsConstructor
public class JpaEmployeeRepository implements EmployeeRepository {

    private final EmployeeRepositoryJpa jpa;

    @Override
    public void addEmployee(Employee employee) {
        jpa.save(EmployeeMapper.toJpa(employee));
    }

    @Override
    public Employee findsEmployee(EmployeeId id) {
        return jpa.findById(id.id())
                .map(EmployeeMapper::toDomain)
                .orElseThrow(() -> new IllegalStateException("Employee not found"));
    }

    @Override
    public List<Employee> findAllEmployees() {
        return jpa.findAll()
                .stream()
                .map(EmployeeMapper::toDomain)
                .toList();
    }

    @Override
    public void removeEmployee(EmployeeId id) {
        jpa.deleteById(id.id());
    }

    @Override
    public boolean existsByUsername(String username) {
        return jpa.findByUsername(username).isPresent();
    }

    @Override
    public Object findsByUsername(String username) {
        return jpa.findByUsername(username);
    }
}
