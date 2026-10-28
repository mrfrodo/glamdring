package com.frodo.glamdring.domain.models.sd;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Core domain model for the System Design Catalog — a reusable system design
 * pattern, shown through one classic example, that can serve as a template
 * for designing many other systems.
 */
public class SystemDesign {

    static final int MAX_BLUEPRINT_LENGTH = 500;
    static final int MIN_COMPONENTS = 2;

    private final SystemDesignId id;
    private final String name;
    private final String classicExample;
    private final String coreBlueprint;
    private final List<KeyComponent> keyComponents;
    private final List<String> reusableFor;

    private SystemDesign(Builder builder) {
        this.id = Objects.requireNonNull(builder.id, "id must not be null");
        this.name = requireText(builder.name, "name");
        this.classicExample = requireText(builder.classicExample, "classicExample");
        this.coreBlueprint = requireText(builder.coreBlueprint, "coreBlueprint");
        if (coreBlueprint.length() > MAX_BLUEPRINT_LENGTH) {
            throw new IllegalArgumentException("Pattern " + id + ": coreBlueprint must be at most "
                    + MAX_BLUEPRINT_LENGTH + " characters — name the essence, not every detail");
        }
        if (builder.keyComponents.size() < MIN_COMPONENTS) {
            throw new IllegalArgumentException("Pattern " + id + " needs at least " + MIN_COMPONENTS
                    + " key components — one component is not a design");
        }
        if (builder.reusableFor.isEmpty()) {
            throw new IllegalArgumentException("Pattern " + id + " must be reusable for at least one system");
        }
        this.keyComponents = List.copyOf(builder.keyComponents);
        this.reusableFor = List.copyOf(builder.reusableFor);
    }

    private static String requireText(String value, String field) {
        Objects.requireNonNull(value, field + " must not be null");
        if (value.isBlank()) throw new IllegalArgumentException(field + " must not be blank");
        return value;
    }

    public static Builder builder() {
        return new Builder();
    }

    public SystemDesignId getId() { return id; }
    public String getName() { return name; }
    public String getClassicExample() { return classicExample; }
    public String getCoreBlueprint() { return coreBlueprint; }
    public List<KeyComponent> getKeyComponents() { return keyComponents; }
    public List<String> getReusableFor() { return reusableFor; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SystemDesign p)) return false;
        return id.equals(p.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SystemDesign{id=" + id + ", name='" + name + "'}";
    }

    public static class Builder {
        private SystemDesignId id;
        private String name;
        private String classicExample;
        private String coreBlueprint;
        private final List<KeyComponent> keyComponents = new ArrayList<>();
        private final List<String> reusableFor = new ArrayList<>();

        public Builder id(String slug) { this.id = new SystemDesignId(slug); return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder classicExample(String classicExample) { this.classicExample = classicExample; return this; }
        public Builder coreBlueprint(String coreBlueprint) { this.coreBlueprint = coreBlueprint; return this; }
        public Builder component(String name, String role, String... technologies) {
            this.keyComponents.add(new KeyComponent(name, role, List.of(technologies)));
            return this;
        }
        public Builder reusableFor(String... systems) { this.reusableFor.addAll(List.of(systems)); return this; }

        public SystemDesign build() { return new SystemDesign(this); }
    }
}
