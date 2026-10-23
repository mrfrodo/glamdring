package com.frodo.glamdring.application.ports.out;

import com.frodo.glamdring.domain.models.sd.SystemDesign;

import java.util.List;

/**
 * Outbound port for retrieving the system design patterns in the System Design
 * Catalog. The current adapter is a static, hand-edited list — new patterns
 * are added by editing the adapter and redeploying, not through any UI.
 */
public interface SDCRepositoryPort {
    List<SystemDesign> findAll();
}
