package com.frodo.glamdring.application.applicationservices;

import com.frodo.glamdring.application.ports.in.GetKillLogUseCase;
import com.frodo.glamdring.application.ports.out.KillLogRepositoryPort;
import com.frodo.glamdring.domain.models.Kill;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Application service implementing the GetKillLogUseCase inbound port.
 * Orchestrates reading the log and ordering it — newest kill first.
 */
@Service
public class KillLogApplicationService implements GetKillLogUseCase {

    private final KillLogRepositoryPort killLogRepositoryPort;

    public KillLogApplicationService(KillLogRepositoryPort killLogRepositoryPort) {
        this.killLogRepositoryPort = killLogRepositoryPort;
    }

    @Override
    public List<Kill> getKills() {
        return killLogRepositoryPort.findAll().stream()
                .sorted(Comparator.comparing(Kill::getSlainOn).reversed())
                .toList();
    }
}
