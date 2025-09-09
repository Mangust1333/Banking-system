package com.kubancevvladislav.repositories;

import com.kubancevvladislav.domain.UserDetailsImpl;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public interface UserRepository {
    Optional<UserDetailsImpl> findByUsername(String username);
    UserDetailsImpl save(UserDetailsImpl user);
}
