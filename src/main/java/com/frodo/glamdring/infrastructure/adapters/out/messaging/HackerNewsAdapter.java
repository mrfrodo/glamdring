package com.frodo.glamdring.infrastructure.adapters.out.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.frodo.glamdring.application.ports.out.ExternalTechFeedPort;
import com.frodo.glamdring.domain.models.Tech;
import com.frodo.glamdring.domain.models.TechTopic;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Reads the current Hacker News front page via the public Algolia HN Search
 * API (https://hn.algolia.com/api) — an official, free, no-auth JSON API,
 * not an HTML scrape. The front page is HN's own "what's trending right now"
 * signal, so filtering it by topic keyword gives a genuinely representative
 * top-N rather than just the latest post mentioning a term.
 */
@Component
@Primary
public class HackerNewsAdapter implements ExternalTechFeedPort {

    private static final String HN_API_BASE = "https://hn.algolia.com";
    private static final String FRONT_PAGE_PATH = "/api/v1/search";
    private static final String SOURCE = "Hacker News";
    private static final int FRONT_PAGE_SIZE = 100;

    private final RestClient restClient;

    public HackerNewsAdapter(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.baseUrl(HN_API_BASE).build();
    }

    @Override
    public List<Tech> fetchByTopic(TechTopic topic, int limit) {
        try {
            AlgoliaSearchResponse response = restClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path(FRONT_PAGE_PATH)
                            .queryParam("tags", "front_page")
                            .queryParam("hitsPerPage", FRONT_PAGE_SIZE)
                            .build())
                    .retrieve()
                    .body(AlgoliaSearchResponse.class);

            if (response == null || response.hits() == null) {
                return Collections.emptyList();
            }

            String needle = topic.getSearchTerm().toLowerCase(Locale.ROOT);

            return response.hits().stream()
                    .filter(hit -> hit.title() != null && hit.title().toLowerCase(Locale.ROOT).contains(needle))
                    .limit(limit)
                    .map(hit -> mapToTech(hit, topic))
                    .toList();

        } catch (Exception e) {
            System.err.println("[HackerNewsAdapter] fetch failed for topic " + topic + ": " + e.getClass().getSimpleName() + " — " + e.getMessage());
            return Collections.emptyList();
        }
    }

    private Tech mapToTech(AlgoliaHit hit, TechTopic topic) {
        String title = hit.title();
        String summary = title + " (" + (hit.points() != null ? hit.points() : 0) + " points, "
                + (hit.numComments() != null ? hit.numComments() : 0) + " comments)";
        String id = hit.objectID() != null ? hit.objectID() : UUID.randomUUID().toString();

        return Tech.builder()
                .id(id)
                .title(title)
                .summary(summary)
                .topic(topic)
                .publishedAt(parseInstant(hit.createdAt()))
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
    record AlgoliaSearchResponse(List<AlgoliaHit> hits) {}

    record AlgoliaHit(
            String objectID,
            String title,
            Integer points,
            @JsonProperty("num_comments") Integer numComments,
            @JsonProperty("created_at") String createdAt
    ) {}
}
