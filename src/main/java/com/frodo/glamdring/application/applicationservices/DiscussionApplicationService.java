package com.frodo.glamdring.application.applicationservices;

import com.frodo.glamdring.application.ports.in.GetDiscussionUseCase;
import com.frodo.glamdring.application.ports.out.DiscussionInitiatorPort;
import com.frodo.glamdring.application.ports.out.DiscussionRepositoryPort;
import com.frodo.glamdring.application.ports.out.DiscussionResponderPort;
import com.frodo.glamdring.domain.models.Discussion;
import com.frodo.glamdring.domain.models.DiscussionId;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

/**
 * Application service implementing the GetDiscussionUseCase inbound port.
 * Orchestrates the one-shot exchange between the initiator and responder
 * adapters and caches the result — never talks to either LLM directly.
 */
@Service
public class DiscussionApplicationService implements GetDiscussionUseCase {

    private final DiscussionInitiatorPort discussionInitiatorPort;
    private final DiscussionResponderPort discussionResponderPort;
    private final DiscussionRepositoryPort discussionRepositoryPort;

    public DiscussionApplicationService(
            DiscussionInitiatorPort discussionInitiatorPort,
            DiscussionResponderPort discussionResponderPort,
            DiscussionRepositoryPort discussionRepositoryPort) {
        this.discussionInitiatorPort = discussionInitiatorPort;
        this.discussionResponderPort = discussionResponderPort;
        this.discussionRepositoryPort = discussionRepositoryPort;
    }

    @Override
    public Optional<Discussion> getDiscussion() {
        return discussionRepositoryPort.find();
    }

    /**
     * Runs the one-shot exchange — initiator opens, responder answers once —
     * and caches the concluded discussion. Called once at startup. Nothing
     * is cached unless both turns succeed, so callers never see a discussion
     * stuck half-open.
     */
    public void startDiscussion() {
        String opening = discussionInitiatorPort.initiate();
        Discussion discussion = Discussion.open(new DiscussionId(UUID.randomUUID().toString()), opening);

        String reply = discussionResponderPort.respond(opening);
        discussion.reply(reply);

        discussionRepositoryPort.save(discussion);
    }
}
