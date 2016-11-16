package ru.steamtanks.mechanics;

import org.jetbrains.annotations.NotNull;
import ru.steamtanks.mechanics.AbstractGameSession;
import ru.steamtanks.mechanics.avatar.UserGameProfile;
import ru.steamtanks.models.UserProfile;

import java.util.concurrent.atomic.AtomicLong;


public class GameSession implements AbstractGameSession {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private final @NotNull Integer sessionId;
    private final @NotNull UserGameProfile first;
    private final @NotNull UserGameProfile second;

    public GameSession(@NotNull UserProfile user1, @NotNull UserProfile user2) {
        this.sessionId = Math.toIntExact(ID_GENERATOR.getAndIncrement());
        this.first = new UserGameProfile(user1);
        this.second =  new UserGameProfile(user2);
    }

    @Override
    public @NotNull UserGameProfile getEnemy(@NotNull UserGameProfile user) {
        if (user == first) {
            return second;
        }
        if (user == second) {
            return first;
        }
        throw new IllegalArgumentException("Requested enemy for game but user not participant");
    }

    @Override
    public @NotNull UserGameProfile getSelf(@NotNull Integer userId) {
        if (first.getUserProfile().getId().equals(userId)) {
            return first;
        }
        if (second.getUserProfile().getId().equals(userId)) {
            return second;
        }
        throw new IllegalArgumentException("Request self for game but user not participate it");
    }

    @Override
    public @NotNull UserGameProfile getFirst() {
        return first;
    }

    @Override
    public @NotNull UserGameProfile getSecond() {
        return second;
    }

/*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameSession that = (GameSession) o;

        return sessionId.equals(that.sessionId);

    }
*/

/*
    @Override
    public int hashCode() {
        return sessionId.hashCode();
    }
*/
}
