package com.frodo.glamdring.infrastructure.adapters.out.persistence;

import com.frodo.glamdring.application.ports.out.SDCRepositoryPort;
import com.frodo.glamdring.domain.models.sd.SystemDesign;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JDBC adapter for {@link SDCRepositoryPort}. Each system design is one aggregate
 * spread over four tables; the child tables use {@code id} for the owning design
 * and {@code position} to keep the order the author entered.
 */
@Component
public class JdbcSDCRepository implements SDCRepositoryPort {

    private final JdbcClient jdbcClient;

    public JdbcSDCRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    @Override
    public List<SystemDesign> findAll() {
        Map<Integer, List<KeyComponentRow>> keyComponents = findKeyComponents();
        Map<Integer, List<String>> reuse = findReuse();

        return jdbcClient.sql("SELECT * FROM system_design ORDER BY id")
                .query((rs, rowNum) -> {
                    int id = rs.getInt("id");
                    SystemDesign.Builder builder = SystemDesign.builder()
                            .id(id)
                            .name(rs.getString("name"))
                            .classicExample(rs.getString("classic_example"))
                            .coreBlueprint(rs.getString("core_blueprint"))
                            .reusableFor(reuse.getOrDefault(id, List.of()).toArray(String[]::new));
                    for (KeyComponentRow component : keyComponents.getOrDefault(id, List.of())) {
                        builder.component(component.name(), component.role(),
                                component.technologies().toArray(String[]::new));
                    }
                    return builder.build();
                })
                .list();
    }

    private Map<Integer, List<KeyComponentRow>> findKeyComponents() {
        Map<String, List<String>> technologies = new HashMap<>();
        jdbcClient.sql("SELECT * FROM system_design_technology ORDER BY id, component_position, position")
                .query(rs -> {
                    technologies.computeIfAbsent(rs.getInt("id") + ":" + rs.getInt("component_position"),
                            key -> new ArrayList<>()).add(rs.getString("technology"));
                });

        Map<Integer, List<KeyComponentRow>> components = new HashMap<>();
        jdbcClient.sql("SELECT * FROM system_design_keycomponent ORDER BY id, position")
                .query(rs -> {
                    int id = rs.getInt("id");
                    components.computeIfAbsent(id, key -> new ArrayList<>()).add(new KeyComponentRow(
                            rs.getString("name"),
                            rs.getString("role"),
                            technologies.getOrDefault(id + ":" + rs.getInt("position"), List.of())));
                });
        return components;
    }

    private Map<Integer, List<String>> findReuse() {
        Map<Integer, List<String>> reuse = new HashMap<>();
        jdbcClient.sql("SELECT * FROM system_design_reuse ORDER BY id, position")
                .query(rs -> {
                    reuse.computeIfAbsent(rs.getInt("id"), key -> new ArrayList<>()).add(rs.getString("target"));
                });
        return reuse;
    }

    private record KeyComponentRow(String name, String role, List<String> technologies) {
    }
}
