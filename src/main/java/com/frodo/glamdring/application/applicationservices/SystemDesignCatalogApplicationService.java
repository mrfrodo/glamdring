package com.frodo.glamdring.application.applicationservices;

import com.frodo.glamdring.application.ports.in.GetSystemDesignCatalogUseCase;
import com.frodo.glamdring.application.ports.out.SDCRepositoryPort;
import com.frodo.glamdring.domain.models.sd.SystemDesign;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Application service implementing the GetSystemDesignCatalogUseCase inbound port.
 * Orchestrates reading the catalog; the filtering rules live in SystemDesign.
 */
@Service
public class SystemDesignCatalogApplicationService implements GetSystemDesignCatalogUseCase {

    private final SDCRepositoryPort systemDesignCatalogRepositoryPort;

    public SystemDesignCatalogApplicationService(SDCRepositoryPort systemDesignCatalogRepositoryPort) {
        this.systemDesignCatalogRepositoryPort = systemDesignCatalogRepositoryPort;
    }

    @Override
    public List<SystemDesign> getSystemDesigns() {
        return systemDesignCatalogRepositoryPort.findAll();
    }

    @Override
    public List<SystemDesign> getPatternsUsing(String componentName) {
        return getSystemDesigns().stream()
                .filter(pattern -> pattern.uses(componentName))
                .toList();
    }

    @Override
    public List<SystemDesign> getPatternsReusableFor(String system) {
        return getSystemDesigns().stream()
                .filter(pattern -> pattern.isReusableFor(system))
                .toList();
    }
}
