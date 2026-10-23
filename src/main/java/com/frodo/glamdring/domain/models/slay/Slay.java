package com.frodo.glamdring.domain.models.slay;

import com.frodo.glamdring.domain.models.Demo;
import com.frodo.glamdring.domain.models.tech.TechTopic;

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
    private final String smellExample;
    private final String slayExample;
    private final String lessonExplanation;
    private final Demo demo;

    private Slay(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "id must not be null");
        this.title = Objects.requireNonNull(builder.title, "title must not be null");
        this.topic = Objects.requireNonNull(builder.topic, "topic must not be null");
        this.smell = Objects.requireNonNull(builder.smell, "smell must not be null");
        this.theSlay = Objects.requireNonNull(builder.theSlay, "theSlay must not be null");
        this.lesson = Objects.requireNonNull(builder.lesson, "lesson must not be null");
        this.slainOn = Objects.requireNonNull(builder.slainOn, "slainOn must not be null");
        this.smellExample = builder.smellExample == null || builder.smellExample.isBlank()
                ? null : builder.smellExample;
        this.slayExample = builder.slayExample == null || builder.slayExample.isBlank()
                ? null : builder.slayExample;
        this.lessonExplanation = builder.lessonExplanation == null || builder.lessonExplanation.isBlank()
                ? null : builder.lessonExplanation;
        this.demo = builder.demo;
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

    public boolean hasSmellExample() { return smellExample != null; }

    /** Java source illustrating the smell; only call when {@link #hasSmellExample()} is true. */
    public String getSmellExample() {
        if (smellExample == null) throw new IllegalStateException("Slay " + id + " has no smell example");
        return smellExample;
    }

    public boolean hasSlayExample() { return slayExample != null; }

    /** Java source illustrating the fix; only call when {@link #hasSlayExample()} is true. */
    public String getSlayExample() {
        if (slayExample == null) throw new IllegalStateException("Slay " + id + " has no slay example");
        return slayExample;
    }

    public boolean hasLessonExplanation() { return lessonExplanation != null; }

    /** Longer prose unpacking the lesson; only call when {@link #hasLessonExplanation()} is true. */
    public String getLessonExplanation() {
        if (lessonExplanation == null) throw new IllegalStateException("Slay " + id + " has no lesson explanation");
        return lessonExplanation;
    }

    public boolean hasDemo() { return demo != null; }

    /** Larger worked example; only call when {@link #hasDemo()} is true. */
    public Demo getDemo() {
        if (demo == null) throw new IllegalStateException("Slay " + id + " has no demo");
        return demo;
    }

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
        private String smellExample;
        private String slayExample;
        private String lessonExplanation;
        private Demo demo;

        public Builder id(String id) { this.id = new SlayId(id); return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder topic(TechTopic topic) { this.topic = topic; return this; }
        public Builder smell(String smell) { this.smell = smell; return this; }
        public Builder theSlay(String theSlay) { this.theSlay = theSlay; return this; }
        public Builder lesson(String lesson) { this.lesson = lesson; return this; }
        public Builder smellExample(String smellExample) { this.smellExample = smellExample; return this; }
        public Builder slayExample(String slayExample) { this.slayExample = slayExample; return this; }
        public Builder lessonExplanation(String lessonExplanation) { this.lessonExplanation = lessonExplanation; return this; }
        public Builder demo(String summary, String code) { this.demo = new Demo(summary, code); return this; }
        public Builder slainOn(LocalDate slainOn) { this.slainOn = slainOn; return this; }
        public Builder slainOn(int year, int month, int day) { this.slainOn = LocalDate.of(year, month, day); return this; }

        public Slay build() { return new Slay(this); }
    }
}
