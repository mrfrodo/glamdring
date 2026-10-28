package com.frodo.glamdring.application.ports.in;

import com.frodo.glamdring.domain.models.sd.SystemDesign;
import com.frodo.glamdring.domain.models.sd.SystemDesignId;

import java.util.List;
import java.util.Optional;

/**
 * Inbound port for reading the System Design Catalog — reusable system design
 * patterns, browsable in full or one at a time.
 */
public interface GetSystemDesignCatalogUseCase {
    List<SystemDesign> getSystemDesigns();

    Optional<SystemDesign> getSystemDesign(SystemDesignId id);
}
