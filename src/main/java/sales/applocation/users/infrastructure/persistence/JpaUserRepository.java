package sales.applocation.users.infrastructure.persistence;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import sales.applocation.users.domain.User;
import sales.applocation.users.domain.UserId;
import sales.applocation.users.domain.UserRepository;
import sales.applocation.users.infrastructure.mapper.UserMapper;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor

public class JpaUserRepository implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public void save(User user) {
        userJpaRepository.save(UserMapper.toJpa(user));
    }

    @Override
    public List<User> findAll() {
        return userJpaRepository.findAll().stream()
                .map(UserMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<User> findById(UserId id) {
        return userJpaRepository.findById(id.id()).map(UserMapper::toDomain);
    }

    @Override
    public void deleteUser(UserId id) {
        userJpaRepository.deleteById(id.id());
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUsername(username);
    }

    @Override
    public Optional<User> findsByUsername(String username) {
        UserJpaEntity entity = userJpaRepository.findByUsername(username);
        return Optional.ofNullable(UserMapper.toDomain(entity));
    }
}
