package com.frodo.glamdring.application.ports.in;

import com.frodo.glamdring.domain.models.Kill;

import java.util.List;

/**
 * Inbound port for reading the Kills log — case studies of domain
 * complexity that has actually been slain, newest first.
 */
public interface GetKillLogUseCase {
    List<Kill> getKills();
}
