package com.frodo.glamdring.infrastructure.adapters.out.persistence;

import com.frodo.glamdring.application.ports.out.KillLogRepositoryPort;
import com.frodo.glamdring.domain.models.Kill;
import com.frodo.glamdring.domain.models.TechTopic;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Outbound adapter for the Kills log — a hand-written, static list.
 * <p>
 * To publish a new kill: add a Kill.builder() entry below and redeploy.
 * There is deliberately no UI or database for this — entries are rare
 * enough that editing code is simpler than building a CMS for it.
 */
@Component
public class StaticKillLogRepository implements KillLogRepositoryPort {

    private static final List<Kill> KILLS = List.of(

            Kill.builder()
                    .id("blocking-reactor")
                    .title("The Blocking Reactor")
                    .topic(TechTopic.HEXAGONAL_ARCHITECTURE)
                    .smell("BlueskyAdapter used WebClient, Spring's async HTTP client — but called "
                            + ".block() on every request. All the weight of a reactive stack (Netty, "
                            + "native epoll transport, the works), none of the benefit. Just a slow, "
                            + "honest RestClient wearing a costume.")
                    .theKill("Swapped to RestClient — same fluent API, but honest about being "
                            + "synchronous. Dropped spring-boot-starter-webflux entirely.")
                    .lesson("If you're calling .block(), you were never actually being reactive. "
                            + "Don't pay for machinery you don't use.")
                    .slainOn(2026, 9, 5)
                    .build()

    );

    @Override
    public List<Kill> findAll() {
        return KILLS;
    }
}
