package sales.applocation.shared.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import sales.applocation.employee.domain.Employee;
import sales.applocation.employee.domain.EmployeeRepository;
import sales.applocation.employee.infrastructure.mapper.EmployeeMapper;
import sales.applocation.shared.auth.dto.AuthRequest;
import sales.applocation.shared.auth.dto.AuthResponse;
import sales.applocation.shared.security.JwtProperties;
import sales.applocation.users.domain.User;
import sales.applocation.users.domain.UserRepository;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtEncoder jwtEncoder;
    private final JwtProperties jwtProperties;

    public AuthResponse authenticate(AuthRequest authRequest) {
        return userRepository.findsByUsername(authRequest.username())
                .filter(user -> passwordEncoder.matches(authRequest.password(), user.getPassword()))
                .map(this::createUserAuthResponse)
                .orElseGet(() -> employeeRepository.findsByUsername(authRequest.username())
                        .map(EmployeeMapper::toDomain)
                        .filter(emp -> passwordEncoder.matches(authRequest.password(), emp.getPassword()))
                        .map(this::createEmployeeAuthResponse)
                        .orElseThrow(() -> new BadCredentialsException("Invalid username or password")));
    }


    public AuthResponse createUserAuthResponse(User user) {
        String token = generateToken(user.getUsername(), List.of(user.getRole().name()), user.getId().id(), "USER");
        return new AuthResponse(
                token,
                user.getUsername(),
                user.getRole().name(),
                user.getId().id(),
                "USER"
        );
    }


    public AuthResponse createEmployeeAuthResponse(Employee employee) {
        // Assuming Employees default to ADMIN or have their own Role mapping
        String role = "ADMIN";
        String token = generateToken(employee.getUsername(), List.of(role), employee.getId().id(), "EMPLOYEE");
        return new AuthResponse(
                token,
                employee.getUsername(),
                role,
                employee.getId().id(),
                "EMPLOYEE"
        );
    }

    private String generateToken(String username, List<String> roles, UUID id, String type) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtProperties.issuer())
                .issuedAt(now)
                .expiresAt(now.plus(jwtProperties.accessTokenMinutes(), ChronoUnit.MINUTES))
                .subject(username)
                .claim("roles", roles)
                .claim("id", id.toString())
                .claim("type", type)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

}
