package com.frodo.glamdring.application.ports.out;

import com.frodo.glamdring.domain.models.tech.Tech;
import com.frodo.glamdring.domain.models.tech.TechTopic;

import java.util.List;

public interface ExternalTechFeedPort {
    List<Tech> fetchByTopic(TechTopic topic, int limit);
}
