package com.frodo.glamdring.application.ports.in;

import com.frodo.glamdring.domain.models.tech.Tech;

import java.util.List;

public interface GetTechUseCase {
    List<Tech> getTopTrends(int limit);
}
