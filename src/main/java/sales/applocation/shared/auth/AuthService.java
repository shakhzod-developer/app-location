package sales.applocation.shared.auth;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sales.applocation.employee.domain.Employee;
import sales.applocation.employee.domain.EmployeeId;
import sales.applocation.employee.domain.EmployeeRepository;
import sales.applocation.shared.auth.dto.AuthRequest;
import sales.applocation.shared.auth.dto.AuthResponse;
import sales.applocation.shared.security.JwtService;
import sales.applocation.users.domain.UserRepository;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse authenticate(AuthRequest authRequest) {

        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authRequest.username(),
                        authRequest.password()
                )
        );
        CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();

        String jwtToken = jwtService.generateToken(userDetails);

        return new AuthResponse(
                jwtToken,
                userDetails.username(),
                userDetails.role().name(),
                userDetails.id(),
                userDetails.isEmployee() ? "EMPLOYEE" : "USER"
        );
    }

    @Transactional
    public void setEmployeeOnline(EmployeeId employeeId, boolean online) {
            Employee emp = employeeRepository.findsEmployee(employeeId);
            emp.goOnline();
            employeeRepository.addEmployee(emp);

    }
}
