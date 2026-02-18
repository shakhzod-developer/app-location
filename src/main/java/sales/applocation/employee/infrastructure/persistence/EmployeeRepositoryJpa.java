package sales.applocation.employee.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EmployeeRepositoryJpa extends JpaRepository<EmployeeJpaEntity, UUID> {

    Object findByUsername(String username);

}
