package com.frodo.glamdring.infrastructure.adapters.out.persistence;

import com.frodo.glamdring.application.ports.out.DiscussionRepositoryPort;
import com.frodo.glamdring.domain.models.Discussion;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Outbound adapter implementing DiscussionRepositoryPort as a simple
 * in-memory cache. A single discussion, generated at startup, lost on
 * restart — same trade-off as the tip of the day.
 */
@Component
public class InMemoryDiscussionRepository implements DiscussionRepositoryPort {

    private final AtomicReference<Discussion> current = new AtomicReference<>();

    @Override
    public void save(Discussion discussion) {
        current.set(discussion);
    }

    @Override
    public Optional<Discussion> find() {
        return Optional.ofNullable(current.get());
    }
}
