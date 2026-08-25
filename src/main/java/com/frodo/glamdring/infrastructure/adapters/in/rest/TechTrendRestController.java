package com.frodo.glamdring.infrastructure.adapters.in.rest;

import com.frodo.glamdring.application.ports.in.GetTechTrendsUseCase;
import com.frodo.glamdring.domain.models.TechTrend;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Inbound REST adapter — exposes GET /api/trends for the frontend JS to poll.
 * Depends only on the inbound port interface, never on the application service directly.
 */
@RestController
@RequestMapping("/api/trends")
public class TechTrendRestController {

    private static final int DEFAULT_LIMIT = 5;
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMM dd HH:mm").withZone(ZoneOffset.UTC);

    private final GetTechTrendsUseCase getTechTrendsUseCase;

    public TechTrendRestController(GetTechTrendsUseCase getTechTrendsUseCase) {
        this.getTechTrendsUseCase = getTechTrendsUseCase;
    }

    @GetMapping
    public ResponseEntity<List<TechTrendDto>> getTopTrends(
            @RequestParam(defaultValue = "5") int limit) {
        List<TechTrendDto> dtos = getTechTrendsUseCase.getTopTrends(limit).stream()
                .map(this::toDto)
                .toList();
        return ResponseEntity.ok(dtos);
    }

    private TechTrendDto toDto(TechTrend trend) {
        return new TechTrendDto(
                trend.getTitle(),
                trend.getSummary(),
                trend.getTopic().name().replace("_", " "),
                FORMATTER.format(trend.getPublishedAt())
        );
    }

    record TechTrendDto(String title, String summary, String topic, String publishedAt) {}
}
