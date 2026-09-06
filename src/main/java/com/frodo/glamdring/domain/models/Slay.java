package com.frodo.glamdring.domain.models;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Core domain model for the "Slay log" — a short case study of one real
 * piece of domain/architectural complexity that was found and fixed.
 * Each Slay documents a smell, the fix that slew it, and the lesson
 * worth keeping — the tagline "slay domain complexity" made literal.
 */
public class Slay {

    private final SlayId id;
    private final String title;
    private final TechTopic topic;
    private final String smell;
    private final String theSlay;
    private final String lesson;
    private final LocalDate slainOn;

    private Slay(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "id must not be null");
        this.title = Objects.requireNonNull(builder.title, "title must not be null");
        this.topic = Objects.requireNonNull(builder.topic, "topic must not be null");
        this.smell = Objects.requireNonNull(builder.smell, "smell must not be null");
        this.theSlay = Objects.requireNonNull(builder.theSlay, "theSlay must not be null");
        this.lesson = Objects.requireNonNull(builder.lesson, "lesson must not be null");
        this.slainOn = Objects.requireNonNull(builder.slainOn, "slainOn must not be null");
    }

    public static Builder builder() {
        return new Builder();
    }

    public SlayId getId() { return id; }
    public String getTitle() { return title; }
    public TechTopic getTopic() { return topic; }
    public String getSmell() { return smell; }
    public String getTheSlay() { return theSlay; }
    public String getLesson() { return lesson; }
    public LocalDate getSlainOn() { return slainOn; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Slay s)) return false;
        return id.equals(s.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Slay{id=" + id + ", title='" + title + "', topic=" + topic + ", slainOn=" + slainOn + "}";
    }

    public static class Builder {
        private SlayId id;
        private String title;
        private TechTopic topic;
        private String smell;
        private String theSlay;
        private String lesson;
        private LocalDate slainOn;

        public Builder id(String id) { this.id = new SlayId(id); return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder topic(TechTopic topic) { this.topic = topic; return this; }
        public Builder smell(String smell) { this.smell = smell; return this; }
        public Builder theSlay(String theSlay) { this.theSlay = theSlay; return this; }
        public Builder lesson(String lesson) { this.lesson = lesson; return this; }
        public Builder slainOn(LocalDate slainOn) { this.slainOn = slainOn; return this; }
        public Builder slainOn(int year, int month, int day) { this.slainOn = LocalDate.of(year, month, day); return this; }

        public Slay build() { return new Slay(this); }
    }
}
