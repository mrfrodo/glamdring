package com.frodo.glamdring.application.ports.out;

import com.frodo.glamdring.domain.models.Tech;
import com.frodo.glamdring.domain.models.TechId;

import java.util.List;
import java.util.Optional;

public interface TechRepositoryPort {

    void save(Tech trend);

    void saveAll(List<Tech> trends);

    Optional<Tech> findById(TechId id);

    List<Tech> findAll();

    List<Tech> findTopNOrderedByPublishedAtDesc(int limit);

    boolean existsById(TechId id);

    void deleteAll();
}
