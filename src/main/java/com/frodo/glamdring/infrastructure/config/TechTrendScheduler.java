package com.frodo.glamdring.infrastructure.config;

import com.frodo.glamdring.application.applicationservices.TechTrendApplicationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Scheduled job that triggers the tech trend refresh every minute.
 * Infrastructure concern — delegates entirely to the application service.
 */
@Component
public class TechTrendScheduler {

    private final TechTrendApplicationService techTrendApplicationService;

    public TechTrendScheduler(TechTrendApplicationService techTrendApplicationService) {
        this.techTrendApplicationService = techTrendApplicationService;
    }

    @Scheduled(fixedRateString = "${glamdring.scheduler.refresh-rate-ms:60000}")
    public void refreshTechTrends() {
        //techTrendApplicationService.refreshTrends();
        System.out.println("dummu refresh");
    }
}
