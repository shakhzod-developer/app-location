package sales.applocation.users.domain;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(UserId id);

    void save(User user);

    List<User> findAll();

    boolean existsByUsername(String username);

    void deleteUser(UserId id);

    User findsByUsername(String username);
}
