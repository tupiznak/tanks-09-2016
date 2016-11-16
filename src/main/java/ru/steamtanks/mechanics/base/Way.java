package ru.steamtanks.mechanics.base;

import org.jetbrains.annotations.NotNull;

public enum Way {
    Left(new Direction(Math.PI)),
    Right(new Direction(0)),
    Up(new Direction(Math.PI /2)),
    Down(new Direction(3 * Math.PI / 2)),
    None(Direction.NO_WHERE);

    private @NotNull Direction radial;

    Way(@NotNull Direction radial) {
        this.radial = radial;
    }

    public @NotNull Direction getRadial() {
        return radial;
    }
}
