package ru.steamtanks.mechanics.base;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServerSnap {

    @NotNull List<ServerPlayerSnap> players;
    private long serverFrameTime;

    public @NotNull List<ServerPlayerSnap> getPlayers() {
        return players;
    }

    public void setPlayers(@NotNull List<ServerPlayerSnap> players) {
        this.players = players;
    }

    public long getServerFrameTime() {
        return serverFrameTime;
    }

    public void setServerFrameTime(long serverFrameTime) {
        this.serverFrameTime = serverFrameTime;
    }
}
