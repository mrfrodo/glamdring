package com.frodo.glamdring.infrastructure.adapters.out.persistence;

import com.frodo.glamdring.application.ports.out.SDCRepositoryPort;
import com.frodo.glamdring.domain.models.sd.SystemDesign;
import com.frodo.glamdring.domain.models.sd.SystemDesignId;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Component;
import tools.jackson.databind.json.JsonMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * File adapter for {@link SDCRepositoryPort}. Each system design is one JSON file
 * in {@code src/main/resources/sdcs}; the file name (minus {@code .json}) is its id.
 * All files are read once at startup, so a file that breaks a domain rule stops
 * the application instead of breaking the catalog page later.
 */
@Component
public class JsonSDCRepository implements SDCRepositoryPort {

    private static final String LOCATION = "classpath:sdcs/*.json";

    private final List<SystemDesign> systemDesigns;

    public JsonSDCRepository(JsonMapper jsonMapper) {
        this.systemDesigns = loadAll(jsonMapper);
    }

    @Override
    public List<SystemDesign> findAll() {
        return systemDesigns;
    }

    @Override
    public Optional<SystemDesign> findById(SystemDesignId id) {
        return systemDesigns.stream()
                .filter(systemDesign -> systemDesign.getId().equals(id))
                .findFirst();
    }

    private static List<SystemDesign> loadAll(JsonMapper jsonMapper) {
        try {
            Resource[] files = new PathMatchingResourcePatternResolver().getResources(LOCATION);
            return Arrays.stream(files)
                    .map(file -> load(jsonMapper, file))
                    .sorted(Comparator.comparing(SystemDesign::getName))
                    .toList();
        } catch (IOException e) {
            throw new UncheckedIOException("Could not list system design files at " + LOCATION, e);
        }
    }

    private static SystemDesign load(JsonMapper jsonMapper, Resource file) {
        String fileName = Objects.requireNonNull(file.getFilename(), "system design file has no name");
        try (InputStream json = file.getInputStream()) {
            return jsonMapper.readValue(json, SystemDesignJson.class)
                    .toDomain(fileName.substring(0, fileName.length() - ".json".length()));
        } catch (IOException | RuntimeException e) {
            throw new IllegalStateException("Invalid system design file sdc/" + fileName + ": " + e.getMessage(), e);
        }
    }

    /** The JSON file format. Kept private so the domain model carries no serialization concerns. */
    private record SystemDesignJson(String name,
                                    String classicExample,
                                    String coreBlueprint,
                                    List<KeyComponentJson> keyComponents,
                                    List<String> reusableFor) {

        SystemDesign toDomain(String slug) {
            SystemDesign.Builder builder = SystemDesign.builder()
                    .id(slug)
                    .name(name)
                    .classicExample(classicExample)
                    .coreBlueprint(coreBlueprint)
                    .reusableFor(orEmpty(reusableFor).toArray(String[]::new));
            for (KeyComponentJson component : orEmpty(keyComponents)) {
                builder.component(component.name(), component.role(),
                        orEmpty(component.technologies()).toArray(String[]::new));
            }
            return builder.build();
        }

        private static <T> List<T> orEmpty(List<T> list) {
            return list == null ? List.of() : list;
        }
    }

    private record KeyComponentJson(String name, String role, List<String> technologies) {
    }
}
