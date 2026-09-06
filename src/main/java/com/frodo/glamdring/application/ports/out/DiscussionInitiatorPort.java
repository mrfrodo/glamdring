package com.frodo.glamdring.application.ports.out;

/**
 * Outbound port for the participant that opens a {@link com.frodo.glamdring.domain.models.Discussion}.
 */
public interface DiscussionInitiatorPort {
    String initiate();
}
