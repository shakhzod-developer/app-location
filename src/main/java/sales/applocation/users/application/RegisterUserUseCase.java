package sales.applocation.users.application;

import org.springframework.security.crypto.password.PasswordEncoder;
import sales.applocation.shared.role.Role;
import sales.applocation.users.application.dto.UserCreateRequest;
import sales.applocation.users.domain.User;
import sales.applocation.users.domain.UserId;
import sales.applocation.users.domain.UserRepository;

import java.util.UUID;

public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterUserUseCase(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public void registerUser(UserCreateRequest request) {
        if (userRepository.existsByUsername(request.username())){
            throw new RuntimeException("Username already exists");
        }

        User user = new User.UserBuilder()
                .id(new UserId(UUID.randomUUID()))
                .username(request.username())
                .phone(request.phone())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER).build();

        userRepository.save(user);
    }
}
