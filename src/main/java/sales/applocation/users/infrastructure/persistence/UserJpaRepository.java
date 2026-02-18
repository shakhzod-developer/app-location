package sales.applocation.users.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import sales.applocation.users.domain.User;
import sales.applocation.users.domain.UserId;

import java.util.Optional;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {


    UserJpaEntity findByUsername(String username);

    boolean existsByUsername(String username);
}
