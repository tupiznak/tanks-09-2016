package ru.steamtanks.mechanics;

import org.jetbrains.annotations.NotNull;
import ru.steamtanks.mechanics.avatar.UserGameProfile;

public interface AbstractGameSession {
    @NotNull UserGameProfile getEnemy(@NotNull UserGameProfile user);

    @NotNull UserGameProfile getSelf(@NotNull Integer userId);

    @NotNull UserGameProfile getFirst();

    @NotNull UserGameProfile getSecond();

}