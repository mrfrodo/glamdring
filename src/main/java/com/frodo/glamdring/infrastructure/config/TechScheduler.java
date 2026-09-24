package com.frodo.glamdring.infrastructure.config;

import com.frodo.glamdring.application.applicationservices.TechApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled job that triggers the tech trend refresh every minute.
 * Infrastructure concern — delegates entirely to the application service.
 */
@Component
public class TechScheduler {

    private final TechApplicationService techApplicationService;

    public TechScheduler(TechApplicationService techApplicationService) {
        this.techApplicationService = techApplicationService;
    }

    @Scheduled(initialDelay = 0, fixedRateString = "${glamdring.scheduler.refresh-rate-ms:60000}")
    public void refreshTechTrends() {
        techApplicationService.refreshTrends();
    }
}
