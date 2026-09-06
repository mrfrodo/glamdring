package com.frodo.glamdring.application.ports.in;

import com.frodo.glamdring.domain.models.Slay;

import java.util.List;

/**
 * Inbound port for reading the Slay log — case studies of domain
 * complexity that has actually been slain, newest first.
 */
public interface GetSlayLogUseCase {
    List<Slay> getSlays();
}
