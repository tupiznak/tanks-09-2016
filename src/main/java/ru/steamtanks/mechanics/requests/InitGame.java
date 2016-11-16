package ru.steamtanks.mechanics.requests;

import org.jetbrains.annotations.NotNull;
import ru.steamtanks.mechanics.base.ServerPlayerSnap;

import java.util.List;
import java.util.Map;

public class InitGame {
    @SuppressWarnings("NullableProblems")
    public static final class Request {
        private @NotNull Integer self;
        private @NotNull Integer selfSquareId;
        private @NotNull List<ServerPlayerSnap> players;
        private @NotNull Map<Integer, String> colors;
        private @NotNull Map<Integer, String> gunColors;
        private @NotNull Map<Integer, String> names;

        public Map<Integer, String> getNames() {
            return names;
        }

        public void setNames(Map<Integer, String> names) {
            this.names = names;
        }

        public @NotNull Integer getSelf() {
            return self;
        }

        public void setSelf(@NotNull Integer self) {
            this.self = self;
        }
        public @NotNull List<ServerPlayerSnap> getPlayers() {
            return players;
        }
        public @NotNull void setPlayers(@NotNull List<ServerPlayerSnap> players) {
            this.players = players;
        }
        public @NotNull Map<Integer, String> getColors() {
            return colors;
        }

        public void setColors(@NotNull Map<Integer, String> colors) {
            this.colors = colors;
        }
        public @NotNull Map<Integer, String> getGunColors() {
            return gunColors;
        }

        public void setGunColors(@NotNull Map<Integer, String> gunColors) {
            this.gunColors = gunColors;
        }

        public Integer getSelfSquareId() {
            return selfSquareId;
        }

        public void setSelfSquareId(Integer selfSquareId) {
            this.selfSquareId = selfSquareId;
        }
    }

}
