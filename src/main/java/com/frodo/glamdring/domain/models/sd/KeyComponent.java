package com.frodo.glamdring.domain.models.sd;

import java.util.List;
import java.util.Objects;

/**
 * One building block of a system design pattern: what it is, why it is there,
 * and optionally which technologies typically fill the role. Technologies are
 * optional because the concept outlives the tools.
 *
 * Key componenets could be load balancer, distributed cache, a nosql database, etc
 */
public record KeyComponent(String name, String role, List<String> technologies) {

    public KeyComponent {
        Objects.requireNonNull(name, "name must not be null");
        Objects.requireNonNull(role, "role must not be null");
        if (name.isBlank()) throw new IllegalArgumentException("name must not be blank");
        if (role.isBlank()) throw new IllegalArgumentException("role must not be blank");
        technologies = technologies == null ? List.of() : List.copyOf(technologies);
    }
}
