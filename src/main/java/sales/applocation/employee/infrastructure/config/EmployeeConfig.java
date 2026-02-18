package sales.applocation.employee.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import sales.applocation.employee.application.EmployeeUseCases;
import sales.applocation.employee.application.RegisterEmployeeUseCase;
import sales.applocation.employee.domain.EmployeeRepository;
import sales.applocation.employee.infrastructure.persistence.EmployeeRepositoryJpa;
import sales.applocation.employee.infrastructure.persistence.JpaEmployeeRepository;

@Configuration
public class EmployeeConfig {

    @Bean
    public EmployeeRepository employeeRepository(EmployeeRepositoryJpa employeeRepositoryJpa) {
        return new JpaEmployeeRepository(employeeRepositoryJpa);
    }

    @Bean
    public EmployeeUseCases employeeUseCases(EmployeeRepository employeeRepository) {
        return new EmployeeUseCases(employeeRepository);
    }

    @Bean
    public RegisterEmployeeUseCase registerEmployeeUseCase(PasswordEncoder passwordEncoder,
                                                           EmployeeRepository employeeRepository) {
        return new RegisterEmployeeUseCase(employeeRepository, passwordEncoder);
    }
}
