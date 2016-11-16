package ru.steamtanks.mechanics.services.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.steamtanks.exceptions.RemotePointService.RPSCloseSocketSessionException;
import ru.steamtanks.mechanics.GameSession;
import ru.steamtanks.mechanics.avatar.UserGameProfile;
import ru.steamtanks.models.UserProfile;

import java.util.Set;

public interface AbstractGameSessionServise {

    @Nullable GameSession getSessionForUser(@NotNull Integer userId);

    Set<GameSession> getSessions();

    boolean isPlaying(@NotNull Integer userId);

    void notifyGameIsOver(@NotNull GameSession gameSession) throws RPSCloseSocketSessionException;

    GameSession startGame(UserProfile first, UserProfile second);
}
