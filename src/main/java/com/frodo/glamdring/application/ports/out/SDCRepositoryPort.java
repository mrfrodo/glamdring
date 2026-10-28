package com.frodo.glamdring.application.ports.out;

import com.frodo.glamdring.domain.models.sd.SystemDesign;
import com.frodo.glamdring.domain.models.sd.SystemDesignId;

import java.util.List;
import java.util.Optional;

/**
 * Outbound port for retrieving the system design patterns in the System Design
 * Catalog. The current adapter reads one JSON file per design from the classpath —
 * new patterns are added by dropping in a file and restarting, not through any UI.
 */
public interface SDCRepositoryPort {
    List<SystemDesign> findAll();

    Optional<SystemDesign> findById(SystemDesignId id);
}
