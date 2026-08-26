package com.frodo.glamdring.infrastructure.adapters.out.persistence;

import com.frodo.glamdring.application.ports.out.TipOfTheDayRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Outbound adapter implementing TipOfTheDayRepositoryPort as a simple
 * in-memory cache. A single value, refreshed at startup, lost on restart —
 * real persistence would be overkill for this.
 */
@Component
public class InMemoryTipOfTheDayRepository implements TipOfTheDayRepositoryPort {

    private final AtomicReference<String> currentTip = new AtomicReference<>();

    @Override
    public void save(String tip) {
        currentTip.set(tip);
    }

    @Override
    public Optional<String> find() {
        return Optional.ofNullable(currentTip.get());
    }
}
