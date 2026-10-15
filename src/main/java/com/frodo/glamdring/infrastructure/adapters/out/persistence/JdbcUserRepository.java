package com.frodo.glamdring.infrastructure.adapters.out.persistence;

import com.frodo.glamdring.application.ports.out.UserRepositoryPort;
import com.frodo.glamdring.domain.models.PasswordHash;
import com.frodo.glamdring.domain.models.Role;
import com.frodo.glamdring.domain.models.User;
import com.frodo.glamdring.domain.models.UserId;
import com.frodo.glamdring.domain.models.Username;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * JDBC adapter for {@link UserRepositoryPort}. Uses only SQL that runs on both
 * H2 (local) and PostgreSQL (VPS): update-then-insert instead of MERGE.
 */
@Component
public class JdbcUserRepository implements UserRepositoryPort {

    private final JdbcClient jdbcClient;

    public JdbcUserRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    @Transactional
    public User save(User user) {
        UserId id = user.isPersisted() ? update(user) : insert(user);

        jdbcClient.sql("DELETE FROM app_user_role WHERE user_id = :id")
                .param("id", id.value())
                .update();
        for (Role role : user.roles()) {
            jdbcClient.sql("INSERT INTO app_user_role (user_id, role) VALUES (:id, :role)")
                    .param("id", id.value())
                    .param("role", role.name())
                    .update();
        }

        return User.reconstitute(id, user.username(), user.passwordHash(), user.roles(), user.isEnabled());
    }

    private UserId update(User user) {
        jdbcClient.sql("UPDATE app_user SET password_hash = :hash, enabled = :enabled WHERE id = :id")
                .param("id", user.id().value())
                .param("hash", user.passwordHash().value())
                .param("enabled", user.isEnabled())
                .update();
        return user.id();
    }

    private UserId insert(User user) {
        KeyHolder keys = new GeneratedKeyHolder();
        jdbcClient.sql("""
                INSERT INTO app_user (username, password_hash, enabled)
                VALUES (:username, :hash, :enabled)
                """)
                .param("username", user.username().value())
                .param("hash", user.passwordHash().value())
                .param("enabled", user.isEnabled())
                .update(keys, "id");
        return new UserId(Objects.requireNonNull(keys.getKey(), "No generated id returned").intValue());
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jdbcClient.sql("SELECT * FROM app_user WHERE id = :id")
                .param("id", id.value())
                .query(this::mapRow)
                .optional();
    }

    @Override
    public Optional<User> findByUsername(Username username) {
        return jdbcClient.sql("SELECT * FROM app_user WHERE username = :username")
                .param("username", username.value())
                .query(this::mapRow)
                .optional();
    }

    @Override
    public boolean existsByUsername(Username username) {
        Integer count = jdbcClient.sql("SELECT COUNT(*) FROM app_user WHERE username = :username")
                .param("username", username.value())
                .query(Integer.class)
                .single();
        return count != null && count > 0;
    }

    private User mapRow(ResultSet rs, int rowNum) throws SQLException {
        int id = rs.getInt("id");
        Set<Role> roles = jdbcClient.sql("SELECT role FROM app_user_role WHERE user_id = :id")
                .param("id", id)
                .query((r, n) -> Role.valueOf(r.getString("role")))
                .list()
                .stream()
                .collect(Collectors.toCollection(() -> EnumSet.noneOf(Role.class)));
        return User.reconstitute(
                new UserId(id),
                new Username(rs.getString("username")),
                new PasswordHash(rs.getString("password_hash")),
                roles,
                rs.getBoolean("enabled"));
    }
}
