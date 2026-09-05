package com.frodo.glamdring.application.ports.out;

import com.frodo.glamdring.domain.models.Kill;

import java.util.List;

/**
 * Outbound port for retrieving Kills log entries. The current adapter is
 * a static, hand-edited list — new entries are added by editing the
 * adapter and redeploying, not through any UI.
 */
public interface KillLogRepositoryPort {
    List<Kill> findAll();
}
