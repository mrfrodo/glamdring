package com.frodo.glamdring.infrastructure.adapters.out.persistence;

import com.frodo.glamdring.application.ports.out.TechRepositoryPort;
import com.frodo.glamdring.domain.models.Tech;
import com.frodo.glamdring.domain.models.TechId;
import com.frodo.glamdring.domain.models.TechTopic;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class JdbcTechRepository implements TechRepositoryPort {

    private final JdbcClient jdbcClient;

    public JdbcTechRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void save(Tech trend) {
        jdbcClient.sql("""
                MERGE INTO tech AS t
                USING (VALUES (CAST(:id AS VARCHAR(255)), CAST(:title AS VARCHAR(512)),
                               CAST(:summary AS VARCHAR(2048)), CAST(:topic AS VARCHAR(64)),
                               CAST(:publishedAt AS VARCHAR(64)), CAST(:source AS VARCHAR(128))))
                    AS s (id, title, summary, topic, published_at, source)
                ON t.id = s.id
                WHEN MATCHED THEN UPDATE SET
                    title = s.title,
                    summary = s.summary,
                    topic = s.topic,
                    published_at = s.published_at,
                    source = s.source
                WHEN NOT MATCHED THEN INSERT (id, title, summary, topic, published_at, source)
                    VALUES (s.id, s.title, s.summary, s.topic, s.published_at, s.source)
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
    public void saveAll(List<Tech> trends) {
        trends.forEach(this::save);
    }

    @Override
    public Optional<Tech> findById(TechId id) {
        return jdbcClient.sql("SELECT * FROM tech WHERE id = :id")
                .param("id", id.value())
                .query(this::mapRow)
                .optional();
    }

    @Override
    public List<Tech> findAll() {
        return jdbcClient.sql("SELECT * FROM tech ORDER BY published_at DESC")
                .query(this::mapRow)
                .list();
    }

    @Override
    public List<Tech> findTopNOrderedByPublishedAtDesc(int limit) {
        return jdbcClient.sql("SELECT * FROM tech ORDER BY published_at DESC LIMIT :limit")
                .param("limit", limit)
                .query(this::mapRow)
                .list();
    }

    @Override
    public boolean existsById(TechId id) {
        Integer count = jdbcClient.sql("SELECT COUNT(*) FROM tech WHERE id = :id")
                .param("id", id.value())
                .query(Integer.class)
                .single();
        return count != null && count > 0;
    }

    @Override
    public void deleteAll() {
        jdbcClient.sql("DELETE FROM tech").update();
    }

    private Tech mapRow(ResultSet rs, int rowNum) throws SQLException {
        return Tech.builder()
                .id(rs.getString("id"))
                .title(rs.getString("title"))
                .summary(rs.getString("summary"))
                .topic(TechTopic.valueOf(rs.getString("topic")))
                .publishedAt(Instant.parse(rs.getString("published_at")))
                .source(rs.getString("source"))
                .build();
    }
}
