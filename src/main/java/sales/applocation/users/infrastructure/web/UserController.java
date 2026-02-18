package sales.applocation.users.infrastructure.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sales.applocation.users.application.UserUseCases;
import sales.applocation.users.application.dto.UserCreateRequest;
import sales.applocation.users.application.dto.UserResponse;
import sales.applocation.users.domain.User;
import sales.applocation.users.domain.UserId;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserUseCases userUseCases;

    @PostMapping("/createUser")
    public ResponseEntity<Void> createUser(@RequestBody UserCreateRequest request) {
        User user = new User.UserBuilder()
                .id(UserId.value())
                .username(request.username())
                .password(request.password())
                .phone(request.phone())
                .build();

        userUseCases.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @GetMapping("/getUser/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        return userUseCases.getUser(new UserId(id))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/getAllUser")
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userUseCases.getAll());
    }

    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userUseCases.deleteUser(new UserId(id));
        return ResponseEntity.noContent().build();
    }
}
