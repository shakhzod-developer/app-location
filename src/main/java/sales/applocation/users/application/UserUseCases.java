package sales.applocation.users.application;

import sales.applocation.users.application.dto.UserResponse;
import sales.applocation.users.domain.User;
import sales.applocation.users.domain.UserId;
import sales.applocation.users.domain.UserRepository;

import java.util.List;
import java.util.Optional;


public class UserUseCases {

    private final UserRepository userRepository;

    public UserUseCases(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void createUser(User user) {
        userRepository.save(user);
    }

    public Optional<UserResponse> getUser(UserId id) {
        return userRepository.findById(id).map(UserResponse::fromDomain);
    }

    public List<UserResponse> getAll() {
        return userRepository.findAll().stream().map(UserResponse::fromDomain).toList();
    }

    public User findByUsername(String username) {
        return userRepository.findsByUsername(username);
    }

    public void deleteUser(UserId id) {
        userRepository.deleteUser(id);
    }
}
