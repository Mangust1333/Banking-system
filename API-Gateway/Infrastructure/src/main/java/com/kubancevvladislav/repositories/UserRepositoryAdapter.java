package com.kubancevvladislav.repositories;
import com.kubancevvladislav.domain.Role;
import com.kubancevvladislav.domain.UserDetailsImpl;
import com.kubancevvladislav.entities.RoleEntity;
import com.kubancevvladislav.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRepositoryAdapter implements UserRepository {
    private final UserJpaRepository jpaRepository;

    @Override
    public Optional<UserDetailsImpl> findByUsername(String username) {
        return jpaRepository.findByLogin(username).map(this::toDomain);
    }

    @Override
    public UserDetailsImpl save(UserDetailsImpl user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    private UserDetailsImpl toDomain(UserEntity entity) {
        return new UserDetailsImpl(
                entity.getUser_id(),
                entity.getLogin(),
                entity.getPassword(),
                Role.valueOf(entity.getRole().toString())
        );
    }

    private UserEntity toEntity(UserDetailsImpl user) {
        return new UserEntity(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                RoleEntity.valueOf(user.getRole().toString())
        );
    }
}
