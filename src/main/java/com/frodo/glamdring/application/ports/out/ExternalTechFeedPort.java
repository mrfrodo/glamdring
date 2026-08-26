package com.frodo.glamdring.application.ports.out;

import com.frodo.glamdring.domain.models.Tech;
import com.frodo.glamdring.domain.models.TechTopic;

import java.util.List;

public interface ExternalTechFeedPort {
    List<Tech> fetchByTopic(TechTopic topic, int limit);
}
