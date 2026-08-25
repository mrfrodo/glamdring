package com.frodo.glamdring.application.ports.in;

import com.frodo.glamdring.domain.models.TechTrend;

import java.util.List;

public interface GetTechTrendsUseCase {
    List<TechTrend> getTopTrends(int limit);
}
