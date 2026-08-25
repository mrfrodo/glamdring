package com.frodo.glamdring.application.applicationservices;

import com.frodo.glamdring.application.ports.in.GetTechTrendsUseCase;
import com.frodo.glamdring.application.ports.out.ExternalTechFeedPort;
import com.frodo.glamdring.application.ports.out.TechTrendRepositoryPort;
import com.frodo.glamdring.domain.domainservices.TechTrendDomainService;
import com.frodo.glamdring.domain.models.TechTrend;
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
public class TechTrendApplicationService implements GetTechTrendsUseCase {

    private static final int FETCH_PER_TOPIC = 3;

    private final ExternalTechFeedPort externalTechFeedPort;
    private final TechTrendRepositoryPort techTrendRepositoryPort;
    private final TechTrendDomainService techTrendDomainService;

    public TechTrendApplicationService(
            ExternalTechFeedPort externalTechFeedPort,
            TechTrendRepositoryPort techTrendRepositoryPort,
            TechTrendDomainService techTrendDomainService) {
        this.externalTechFeedPort = externalTechFeedPort;
        this.techTrendRepositoryPort = techTrendRepositoryPort;
        this.techTrendDomainService = techTrendDomainService;
    }

    /**
     * Returns the top N most recent trending tech signals from the cache.
     */
    @Override
    public List<TechTrend> getTopTrends(int limit) {
        List<TechTrend> all = techTrendRepositoryPort.findAll();
        return techTrendDomainService.selectTopTrends(all, limit);
    }

    /**
     * Refreshes the cache by fetching from the external feed for all known topics.
     * Called by the scheduler every minute.
     */
    public void refreshTrends() {
        List<TechTrend> fresh = new ArrayList<>();
        for (TechTopic topic : TechTopic.values()) {
            List<TechTrend> fetched = externalTechFeedPort.fetchByTopic(topic, FETCH_PER_TOPIC);
            fetched.stream()
                    .filter(t -> !techTrendRepositoryPort.existsById(t.getId()))
                    .forEach(fresh::add);
        }
        techTrendRepositoryPort.saveAll(fresh);
    }
}
