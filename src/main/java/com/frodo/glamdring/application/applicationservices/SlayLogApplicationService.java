package com.frodo.glamdring.application.applicationservices;

import com.frodo.glamdring.application.ports.in.GetSlayLogUseCase;
import com.frodo.glamdring.application.ports.out.SlayLogRepositoryPort;
import com.frodo.glamdring.domain.models.Slay;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * Application service implementing the GetSlayLogUseCase inbound port.
 * Orchestrates reading the log and ordering it — newest slay first.
 */
@Service
public class SlayLogApplicationService implements GetSlayLogUseCase {

    private final SlayLogRepositoryPort slayLogRepositoryPort;

    public SlayLogApplicationService(SlayLogRepositoryPort slayLogRepositoryPort) {
        this.slayLogRepositoryPort = slayLogRepositoryPort;
    }

    @Override
    public List<Slay> getSlays() {
        return slayLogRepositoryPort.findAll().stream()
                .sorted(Comparator.comparing(Slay::getSlainOn).reversed())
                .toList();
    }
}
