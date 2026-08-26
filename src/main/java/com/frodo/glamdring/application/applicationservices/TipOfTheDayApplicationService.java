package com.frodo.glamdring.application.applicationservices;

import com.frodo.glamdring.application.ports.in.GetTipOfTheDayUseCase;
import com.frodo.glamdring.application.ports.out.TipGeneratorPort;
import com.frodo.glamdring.application.ports.out.TipOfTheDayRepositoryPort;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Application service implementing the GetTipOfTheDayUseCase inbound port.
 * Orchestrates between the LLM tip generator and the cache — does not talk
 * to either directly, only through their ports.
 */
@Service
public class TipOfTheDayApplicationService implements GetTipOfTheDayUseCase {

    private final TipGeneratorPort tipGeneratorPort;
    private final TipOfTheDayRepositoryPort tipOfTheDayRepositoryPort;

    public TipOfTheDayApplicationService(
            TipGeneratorPort tipGeneratorPort,
            TipOfTheDayRepositoryPort tipOfTheDayRepositoryPort) {
        this.tipGeneratorPort = tipGeneratorPort;
        this.tipOfTheDayRepositoryPort = tipOfTheDayRepositoryPort;
    }

    @Override
    public Optional<String> getTip() {
        return tipOfTheDayRepositoryPort.find();
    }

    /**
     * Asks the LLM for a fresh tip and caches it. Called once at startup.
     */
    public void refreshTip() {
        String tip = tipGeneratorPort.generateTip();
        tipOfTheDayRepositoryPort.save(tip);
    }
}
