package com.frodo.glamdring.application.ports.in;

import com.frodo.glamdring.domain.models.Discussion;

import java.util.Optional;

public interface GetDiscussionUseCase {
    Optional<Discussion> getDiscussion();
}
