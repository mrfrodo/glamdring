package com.frodo.glamdring.domain.domainservices;

import com.frodo.glamdring.domain.models.TechTrend;
import com.frodo.glamdring.domain.models.TechTopic;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

public class TechTrendDomainService {

    public List<TechTrend> selectTopTrends(List<TechTrend> candidates, int limit) {
        return candidates.stream()
                .sorted(Comparator.comparing(TechTrend::getPublishedAt).reversed())
                .limit(limit)
                .toList();
    }

    public List<TechTrend> filterFresh(List<TechTrend> trends, Instant threshold) {
        return trends.stream()
                .filter(t -> t.isNewerThan(threshold))
                .toList();
    }

    public List<TechTrend> filterByTopic(List<TechTrend> trends, TechTopic topic) {
        return trends.stream()
                .filter(t -> t.isAbout(topic))
                .toList();
    }
}
