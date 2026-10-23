package com.frodo.glamdring.application.ports.in;

import com.frodo.glamdring.domain.models.sd.SystemDesign;

import java.util.List;

/**
 * Inbound port for reading the System Design Catalog — reusable system design
 * patterns, browsable in full or filtered by component or reuse target.
 */
public interface GetSystemDesignCatalogUseCase {
    List<SystemDesign> getSystemDesigns();

    List<SystemDesign> getPatternsUsing(String componentName);

    List<SystemDesign> getPatternsReusableFor(String system);
}
