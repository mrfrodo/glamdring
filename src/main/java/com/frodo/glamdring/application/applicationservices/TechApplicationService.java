package com.frodo.glamdring.application.applicationservices;

import com.frodo.glamdring.application.ports.in.GetTechUseCase;
import com.frodo.glamdring.application.ports.out.ExternalTechFeedPort;
import com.frodo.glamdring.application.ports.out.TechRepositoryPort;
import com.frodo.glamdring.domain.domainservices.TechDomainService;
import com.frodo.glamdring.domain.models.Tech;
import com.frodo.glamdring.domain.models.TechTopic;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Application service implementing the GetTechTrendsUseCase inbound port.
 * Orchestrates between domain logic, external feed fetching and persistence.
 * Does not contain business rules — those live in TechTrendDomainService.
 */
@Service
public class TechApplicationService implements GetTechUseCase {

    private static final int FETCH_PER_TOPIC = 3;

    private final ExternalTechFeedPort externalTechFeedPort;
    private final TechRepositoryPort techRepositoryPort;
    private final TechDomainService techDomainService;

    public TechApplicationService(
            ExternalTechFeedPort externalTechFeedPort,
            TechRepositoryPort techRepositoryPort,
            TechDomainService techDomainService) {
        this.externalTechFeedPort = externalTechFeedPort;
        this.techRepositoryPort = techRepositoryPort;
        this.techDomainService = techDomainService;
    }

    /**
     * Returns the top N most recent trending tech signals from the cache.
     */
    @Override
    public List<Tech> getTopTrends(int limit) {
        List<Tech> all = techRepositoryPort.findAll();
        return techDomainService.selectTopTrends(all, limit);
    }

    /**
     * Refreshes the cache by fetching from the external feed for all known topics.
     * Called by the scheduler every minute.
     */
    public void refreshTrends() {
        List<Tech> fresh = new ArrayList<>();
        for (TechTopic topic : TechTopic.values()) {
            List<Tech> fetched = externalTechFeedPort.fetchByTopic(topic, FETCH_PER_TOPIC);
            fetched.stream()
                    .filter(t -> !techRepositoryPort.existsById(t.getId()))
                    .forEach(fresh::add);
        }
        techRepositoryPort.saveAll(fresh);
    }
}
