package com.frodo.glamdring.domain.models;

/**
 * The two participants in a {@link Discussion}. The initiator always opens,
 * the responder always answers — the order is part of the domain rule, not
 * just a UI convention.
 */
public enum Speaker {

    INITIATOR("Qwen"),
    RESPONDER("Qwen Coder");

    private final String displayName;

    Speaker(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
