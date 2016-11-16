package ru.steamtanks.mechanics.base;

import org.jetbrains.annotations.NotNull;
import ru.steamtanks.mechanics.game.GameObject;
import ru.steamtanks.mechanics.game.Snap;

public class ServerPlayerSnap {
    private @NotNull Integer userId;

    private Snap<? extends GameObject> playerSquare;

    public Snap<? extends GameObject> getPlayerSquare() {
        return playerSquare;
    }

    public void setPlayerSquare(Snap<? extends GameObject> playerSquare) {
        this.playerSquare = playerSquare;
    }

    public @NotNull Integer getUserId() {
        return userId;
    }

    public void setUserId(@NotNull Integer userId) {
        this.userId = userId;
    }
}
