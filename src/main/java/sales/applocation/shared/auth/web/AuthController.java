package sales.applocation.shared.auth.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sales.applocation.employee.application.RegisterEmployeeUseCase;
import sales.applocation.employee.application.dto.EmployeeCreateRequest;
import sales.applocation.shared.auth.AuthService;
import sales.applocation.shared.auth.dto.AuthRequest;
import sales.applocation.shared.auth.dto.AuthResponse;
import sales.applocation.users.application.RegisterUserUseCase;
import sales.applocation.users.application.dto.UserCreateRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final RegisterEmployeeUseCase employeeUseCase;
    private final RegisterUserUseCase userUseCase;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        AuthResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register/user")
    public ResponseEntity<Void> registerUser(@RequestBody UserCreateRequest request) {
        userUseCase.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register/employee")
    public ResponseEntity<Void> registerEmployee(@RequestBody EmployeeCreateRequest request) {
        employeeUseCase.registerEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
