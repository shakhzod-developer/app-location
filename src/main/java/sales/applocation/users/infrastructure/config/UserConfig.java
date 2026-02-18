package sales.applocation.users.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import sales.applocation.users.application.RegisterUserUseCase;
import sales.applocation.users.application.UserUseCases;
import sales.applocation.users.domain.UserRepository;
import sales.applocation.users.infrastructure.persistence.JpaUserRepository;
import sales.applocation.users.infrastructure.persistence.UserJpaRepository;

@Configuration
public class UserConfig {

    @Bean
    public UserRepository userRepository(UserJpaRepository userJpaRepository) {
        return new JpaUserRepository(userJpaRepository);
    }

    @Bean
    public UserUseCases userUseCases(UserRepository userRepository) {
        return new UserUseCases(userRepository);
    }

    @Bean
    public RegisterUserUseCase  registerUserUseCase(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        return new RegisterUserUseCase(passwordEncoder, userRepository);
    }
}
