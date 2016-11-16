package ru.steamtanks.mechanics.base;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("PublicField")
public class Coords {

    public Coords(@JsonProperty("x") double x, @JsonProperty("y") double y) {
        this.x = x;
        this.y = y;
    }

    public final double x;
    public final double y;

    public @NotNull Coords add(@NotNull Coords addition) {
        return new Coords(x + addition.x, y + addition.y);
    }
}
