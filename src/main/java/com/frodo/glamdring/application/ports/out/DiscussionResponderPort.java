package com.frodo.glamdring.application.ports.out;

/**
 * Outbound port for the participant that answers the opening remark of a
 * {@link com.frodo.glamdring.domain.models.Discussion}.
 */
public interface DiscussionResponderPort {
    String respond(String openingMessage);
}
