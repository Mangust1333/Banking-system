package com.kubancevvladislav.services;

import com.kubancevvladislav.domain.RegistrationDTO;
import com.kubancevvladislav.domain.UserDetailsImpl;
import com.kubancevvladislav.repositories.UserRepository;
import com.kubancevvladislav.services.result.types.UserServiceRegisterResultType;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new UsernameNotFoundException(
                        "Пользователь с логином " + username + " не найден"
                )
        );
    }

    public UserServiceRegisterResultType register(RegistrationDTO request) {
        if (userRepository.findByUsername(request.login()).isPresent()) {
            return UserServiceRegisterResultType.userWithLoginAlreadyExists(request.login());
        }

        UserDetailsImpl user = new UserDetailsImpl();
        user.setUsername(request.login());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole(request.role());
        userRepository.save(user);

        return UserServiceRegisterResultType.success();
    }
}
