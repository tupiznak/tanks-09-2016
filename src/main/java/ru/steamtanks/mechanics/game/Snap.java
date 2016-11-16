package ru.steamtanks.mechanics.game;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

public interface Snap<T> {

    @NotNull
    @JsonProperty("name")
    String getPartName();
}
