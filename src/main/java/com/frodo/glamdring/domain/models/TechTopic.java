package com.frodo.glamdring.domain.models;

/**
 * Enumeration of technology topics the portal tracks.
 * These map directly to search terms used when crawling external feeds.
 */
public enum TechTopic {

    SOFTWARE_ENGINEERING("software engineering"),
    DOMAIN_DRIVEN_DESIGN("domain driven design"),
    HEXAGONAL_ARCHITECTURE("hexagonal architecture"),
    MICROSERVICES("microservices"),
    DEVOPS("devops"),
    CLEAN_ARCHITECTURE("clean architecture"),
    EVENT_DRIVEN("event driven architecture"),
    KAFKA("apache kafka"),
    RUST("rust"),
    KUBERNETES("kubernetes"),
    WEBASSEMBLY("webassembly"),
    GOLANG("golang"),
    TYPESCRIPT("typescript"),
    AI_AGENTS("ai agents");

    private final String searchTerm;

    TechTopic(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm() {
        return searchTerm;
    }
}
