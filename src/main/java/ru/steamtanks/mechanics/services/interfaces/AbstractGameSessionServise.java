package ru.steamtanks.mechanics.services.interfaces;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.steamtanks.exceptions.GameSessionService.GSSUserNotExist;
import ru.steamtanks.exceptions.RemotePointService.RPSCloseSocketSessionException;
import ru.steamtanks.mechanics.GameSession;
import ru.steamtanks.mechanics.avatar.UserGameProfile;
import ru.steamtanks.models.UserProfile;

import java.util.Set;

public interface AbstractGameSessionServise {

    @NotNull GameSession getSessionForUser(@NotNull Integer userId)
            throws GSSUserNotExist;

    Set<GameSession> getSessions();

    boolean isPlaying(@NotNull Integer userId);

    void notifyGameIsOver(@NotNull GameSession gameSession) throws RPSCloseSocketSessionException;

    Integer startGame(@NotNull UserProfile userProfile, Integer maxCountOfUsers);

    void stopGame(@NotNull UserProfile userProfile) throws GSSUserNotExist;
}
