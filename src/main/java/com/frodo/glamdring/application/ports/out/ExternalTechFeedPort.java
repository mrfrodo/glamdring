package com.frodo.glamdring.application.ports.out;

import com.frodo.glamdring.domain.models.TechTrend;
import com.frodo.glamdring.domain.models.TechTopic;

import java.util.List;

public interface ExternalTechFeedPort {
    List<TechTrend> fetchByTopic(TechTopic topic, int limit);
}
