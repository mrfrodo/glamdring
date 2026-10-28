package com.frodo.glamdring.application.applicationservices;

import com.frodo.glamdring.application.ports.in.GetSystemDesignCatalogUseCase;
import com.frodo.glamdring.application.ports.out.SDCRepositoryPort;
import com.frodo.glamdring.domain.models.sd.SystemDesign;
import com.frodo.glamdring.domain.models.sd.SystemDesignId;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Application service implementing the GetSystemDesignCatalogUseCase inbound port.
 * Orchestrates reading the catalog; the rules for a valid design live in SystemDesign.
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
    public Optional<SystemDesign> getSystemDesign(SystemDesignId id) {
        return systemDesignCatalogRepositoryPort.findById(id);
    }
}
