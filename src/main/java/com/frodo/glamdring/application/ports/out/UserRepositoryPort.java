package com.frodo.glamdring.application.ports.out;

import com.frodo.glamdring.domain.models.User;
import com.frodo.glamdring.domain.models.UserId;
import com.frodo.glamdring.domain.models.Username;

import java.util.Optional;

/** Outbound port for persisting {@link User} aggregates. */
public interface UserRepositoryPort {

    Optional<User> findById(UserId id);

    Optional<User> findByUsername(Username username);

    boolean existsByUsername(Username username);

    /** Persists the user and returns it with its database-assigned id. */
    User save(User user);
}
