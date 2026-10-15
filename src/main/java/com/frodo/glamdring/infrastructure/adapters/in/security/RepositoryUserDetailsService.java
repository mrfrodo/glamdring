package com.frodo.glamdring.infrastructure.adapters.in.security;

import com.frodo.glamdring.application.ports.out.UserRepositoryPort;
import com.frodo.glamdring.domain.models.User;
import com.frodo.glamdring.domain.models.Username;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * Adapter that lets Spring Security authenticate against users stored through
 * {@link UserRepositoryPort}. Unknown, blank or disabled users cannot log in.
 */
@Component
public class RepositoryUserDetailsService implements UserDetailsService {

    private final UserRepositoryPort users;

    public RepositoryUserDetailsService(UserRepositoryPort users) {
        this.users = users;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null || username.isBlank()) {
            throw new UsernameNotFoundException("Blank username");
        }
        User user = users.findByUsername(new Username(username))
                .orElseThrow(() -> new UsernameNotFoundException("Unknown user"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.username().value())
                .password(user.passwordHash().value())
                .roles(user.roles().stream().map(Enum::name).toArray(String[]::new))
                .disabled(!user.isEnabled())
                .build();
    }
}
