package com.frodo.glamdring.application.ports.out;

import com.frodo.glamdring.domain.models.TechTrend;
import com.frodo.glamdring.domain.models.TechTrendId;

import java.util.List;
import java.util.Optional;

public interface TechTrendRepositoryPort {

    void save(TechTrend trend);

    void saveAll(List<TechTrend> trends);

    Optional<TechTrend> findById(TechTrendId id);

    List<TechTrend> findAll();

    List<TechTrend> findTopNOrderedByPublishedAtDesc(int limit);

    boolean existsById(TechTrendId id);

    void deleteAll();
}
