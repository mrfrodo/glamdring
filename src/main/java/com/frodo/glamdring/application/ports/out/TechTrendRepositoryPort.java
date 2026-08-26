package com.frodo.glamdring.application.ports.out;

import com.frodo.glamdring.domain.models.Tech;
import com.frodo.glamdring.domain.models.TechTrendId;

import java.util.List;
import java.util.Optional;

public interface TechTrendRepositoryPort {

    void save(Tech trend);

    void saveAll(List<Tech> trends);

    Optional<Tech> findById(TechTrendId id);

    List<Tech> findAll();

    List<Tech> findTopNOrderedByPublishedAtDesc(int limit);

    boolean existsById(TechTrendId id);

    void deleteAll();
}
