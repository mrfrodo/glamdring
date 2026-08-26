package com.frodo.glamdring.application.ports.in;

import java.util.Optional;

public interface GetTipOfTheDayUseCase {
    Optional<String> getTip();
}
