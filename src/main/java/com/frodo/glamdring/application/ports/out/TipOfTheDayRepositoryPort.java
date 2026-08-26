package com.frodo.glamdring.application.ports.out;

import java.util.Optional;

public interface TipOfTheDayRepositoryPort {

    void save(String tip);

    Optional<String> find();
}
