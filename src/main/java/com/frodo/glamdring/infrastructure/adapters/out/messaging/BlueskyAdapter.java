package com.frodo.glamdring.infrastructure.adapters.out.messaging;

import com.frodo.glamdring.application.ports.out.ExternalTechFeedPort;
import com.frodo.glamdring.domain.models.TechTrend;
import com.frodo.glamdring.domain.models.TechTopic;
import com.frodo.glamdring.domain.models.TechTrendId;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class BlueskyAdapter implements ExternalTechFeedPort {

    private static final String BLUESKY_API_BASE = "https://public.api.bsky.app";
    private static final String SEARCH_PATH = "/xrpc/app.bsky.feed.searchPosts";
    private static final String SOURCE = "Bluesky";

    private final WebClient webClient;

    public BlueskyAdapter(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl(BLUESKY_API_BASE).build();
    }

    @Override
    public List<TechTrend> fetchByTopic(TechTopic topic, int limit) {
        try {
            BlueskySearchResponse response = webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(SEARCH_PATH)
                            .queryParam("q", topic.getSearchTerm())
                            .queryParam("limit", limit)
                            .queryParam("sort", "latest")
                            .build())
                    .retrieve()
                    .bodyToMono(BlueskySearchResponse.class)
                    .block();

            if (response == null || response.posts() == null) {
                return Collections.emptyList();
            }

            return response.posts().stream()
                    .filter(post -> post.record() != null && post.record().text() != null)
                    .map(post -> mapToTechTrend(post, topic))
                    .toList();

        } catch (Exception e) {
            System.err.println("[BlueskyAdapter] fetch failed for topic " + topic + ": " + e.getClass().getSimpleName() + " — " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private TechTrend mapToTechTrend(BlueskyPost post, TechTopic topic) {
        String id = post.cid() != null ? post.cid() : UUID.randomUUID().toString();
        String text = post.record().text();
        String title = text.length() > 100 ? text.substring(0, 100) + "…" : text;
        String summary = text.length() > 300 ? text.substring(0, 300) + "…" : text;
        Instant publishedAt = parseInstant(post.record().createdAt());

        return TechTrend.builder()
                .id(new TechTrendId(id))
                .title(title)
                .summary(summary)
                .topic(topic)
                .publishedAt(publishedAt)
                .source(SOURCE)
                .build();
    }

    private Instant parseInstant(String createdAt) {
        try {
            return Instant.parse(createdAt);
        } catch (Exception e) {
            return Instant.now();
        }
    }

    // Internal response records — infrastructure detail, never exposed outside this adapter
    record BlueskySearchResponse(List<BlueskyPost> posts) {}
    record BlueskyPost(String cid, BlueskyRecord record) {}
    record BlueskyRecord(String text, String createdAt) {}
}
