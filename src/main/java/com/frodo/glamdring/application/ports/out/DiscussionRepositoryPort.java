package com.frodo.glamdring.application.ports.out;

import com.frodo.glamdring.domain.models.Discussion;

import java.util.Optional;

public interface DiscussionRepositoryPort {
    void save(Discussion discussion);
    Optional<Discussion> find();
}
