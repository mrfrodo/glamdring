package com.frodo.glamdring.domain.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Core domain model for a short exchange between two AI participants about
 * a matter of tech: the initiator opens with a remark, the responder answers
 * exactly once, and the discussion concludes there — by design, for now.
 * <p>
 * The one-reply-then-done rule is enforced here, not by whoever calls this
 * class, so a Discussion can never be caught in an invalid state such as a
 * reply with no opening, or two replies to the same opening.
 */
public class Discussion {

    private final DiscussionId id;
    private final List<DiscussionTurn> turns = new ArrayList<>();

    private Discussion(DiscussionId id) {
        this.id = Objects.requireNonNull(id, "id must not be null");
    }

    public static Discussion open(DiscussionId id, String openingMessage) {
        Discussion discussion = new Discussion(id);
        discussion.turns.add(new DiscussionTurn(Speaker.INITIATOR, openingMessage, Instant.now()));
        return discussion;
    }

    public void reply(String message) {
        if (turns.isEmpty()) {
            throw new IllegalStateException("Cannot reply before the discussion has been opened");
        }
        if (isConcluded()) {
            throw new IllegalStateException("Discussion is already concluded — no more replies for now");
        }
        turns.add(new DiscussionTurn(Speaker.RESPONDER, message, Instant.now()));
    }

    public boolean isConcluded() {
        return turns.size() >= 2;
    }

    public DiscussionId getId() {
        return id;
    }

    public String getOpeningMessage() {
        return turns.get(0).message();
    }

    public String getReplyMessage() {
        return isConcluded() ? turns.get(1).message() : null;
    }

    public List<DiscussionTurn> getTurns() {
        return List.copyOf(turns);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Discussion d)) return false;
        return id.equals(d.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Discussion{id=" + id + ", turns=" + turns.size() + "}";
    }
}
