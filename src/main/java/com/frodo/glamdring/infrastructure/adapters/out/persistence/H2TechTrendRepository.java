package com.frodo.glamdring.infrastructure.adapters.out.persistence;

import com.frodo.glamdring.application.ports.out.TechTrendRepositoryPort;
import com.frodo.glamdring.domain.models.TechTrend;
import com.frodo.glamdring.domain.models.TechTrendId;
import com.frodo.glamdring.domain.models.TechTopic;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class H2TechTrendRepository implements TechTrendRepositoryPort {

    private final JdbcClient jdbcClient;

    public H2TechTrendRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(TechTrend trend) {
        jdbcClient.sql("""
                MERGE INTO tech_trend (id, title, summary, topic, published_at, source)
                KEY (id)
                VALUES (:id, :title, :summary, :topic, :publishedAt, :source)
                """)
                .param("id", trend.getId().value())
                .param("title", trend.getTitle())
                .param("summary", trend.getSummary())
                .param("topic", trend.getTopic().name())
                .param("publishedAt", trend.getPublishedAt().toString())
                .param("source", trend.getSource())
                .update();
    }

    @Override
    public void saveAll(List<TechTrend> trends) {
        trends.forEach(this::save);
    }

    @Override
    public Optional<TechTrend> findById(TechTrendId id) {
        return jdbcClient.sql("SELECT * FROM tech_trend WHERE id = :id")
                .param("id", id.value())
                .query(this::mapRow)
                .optional();
    }

    @Override
    public List<TechTrend> findAll() {
        return jdbcClient.sql("SELECT * FROM tech_trend ORDER BY published_at DESC")
                .query(this::mapRow)
                .list();
    }

    @Override
    public List<TechTrend> findTopNOrderedByPublishedAtDesc(int limit) {
        return jdbcClient.sql("SELECT * FROM tech_trend ORDER BY published_at DESC LIMIT :limit")
                .param("limit", limit)
                .query(this::mapRow)
                .list();
    }

    @Override
    public boolean existsById(TechTrendId id) {
        Integer count = jdbcClient.sql("SELECT COUNT(*) FROM tech_trend WHERE id = :id")
                .param("id", id.value())
                .query(Integer.class)
                .single();
        return count != null && count > 0;
    }

    @Override
    public void deleteAll() {
        jdbcClient.sql("DELETE FROM tech_trend").update();
    }

    private TechTrend mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TechTrend.builder()
                .id(rs.getString("id"))
                .title(rs.getString("title"))
                .summary(rs.getString("summary"))
                .topic(TechTopic.valueOf(rs.getString("topic")))
                .publishedAt(Instant.parse(rs.getString("published_at")))
                .source(rs.getString("source"))
                .build();
    }
}
