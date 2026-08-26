package com.frodo.glamdring.domain.models;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

/**
 * Core domain model representing a trending technology signal.
 * A TechTrend captures a signal from the tech community — a post, article or discussion
 * that indicates momentum around a given technology topic.
 */
public class Tech {

    private final TechTrendId id;
    private final String title;
    private final String summary;
    private final TechTopic topic;
    private final Instant publishedAt;
    private final String source;

    private Tech(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "id must not be null");
        this.title = Objects.requireNonNull(builder.title, "title must not be null");
        this.summary = Objects.requireNonNull(builder.summary, "summary must not be null");
        this.topic = Objects.requireNonNull(builder.topic, "topic must not be null");
        this.publishedAt = Objects.requireNonNull(builder.publishedAt, "publishedAt must not be null");
        this.source = Objects.requireNonNull(builder.source, "source must not be null");
    }

    public static Builder builder() {
        return new Builder();
    }

    public TechTrendId getId() { return id; }
    public String getTitle() { return title; }
    public String getSummary() { return summary; }
    public TechTopic getTopic() { return topic; }
    public Instant getPublishedAt() { return publishedAt; }
    public String getSource() { return source; }

    public boolean isAbout(TechTopic techTopic) {
        return this.topic == techTopic;
    }

    public boolean isNewerThan(Instant threshold) {
        return publishedAt.isAfter(threshold);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tech t)) return false;
        return id.equals(t.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TechTrend{id=" + id + ", title='" + title + "', topic=" + topic + ", publishedAt=" + publishedAt + "}";
    }

    public static class Builder {
        private TechTrendId id;
        private String title;
        private String summary;
        private TechTopic topic;
        private Instant publishedAt;
        private String source;

        public Builder id(TechTrendId id) { this.id = id; return this; }
        public Builder id(String id) { this.id = new TechTrendId(id); return this; }
        public Builder id(UUID id) { this.id = new TechTrendId(id.toString()); return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder summary(String summary) { this.summary = summary; return this; }
        public Builder topic(TechTopic topic) { this.topic = topic; return this; }
        public Builder publishedAt(Instant publishedAt) { this.publishedAt = publishedAt; return this; }
        public Builder source(String source) { this.source = source; return this; }

        public Tech build() { return new Tech(this); }
    }
}
