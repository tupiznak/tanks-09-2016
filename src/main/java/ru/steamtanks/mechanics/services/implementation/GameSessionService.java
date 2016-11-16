package ru.steamtanks.mechanics.services.implementation;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import ru.steamtanks.exceptions.RemotePointService.RPSCloseSocketSessionException;
import ru.steamtanks.mechanics.GameSession;
import ru.steamtanks.mechanics.services.interfaces.AbstractGameSessionServise;
import ru.steamtanks.models.UserProfile;

import java.util.*;

@Service
public class GameSessionService implements AbstractGameSessionServise{
    private final @NotNull Map<Integer, GameSession> usersMap = new HashMap<>();
    private final @NotNull Set<GameSession> gameSessions = new LinkedHashSet<>();

    private final @NotNull RemotePointService remotePointService;

    private final @NotNull GameInitService gameInitService;

    public GameSessionService(@NotNull RemotePointService remotePointService, @NotNull GameInitService gameInitService) {
        this.remotePointService = remotePointService;
        this.gameInitService = gameInitService;
    }

    @Override
    public Set<GameSession> getSessions() {
        return gameSessions;
    }

    @Override
    public @Nullable GameSession getSessionForUser(@NotNull Integer userId) {
        return usersMap.get(userId);
    }

    @Override
    public boolean isPlaying(@NotNull Integer userId) {
        return usersMap.containsKey(userId);
    }

    @Override
    public void notifyGameIsOver(@NotNull GameSession gameSession) throws RPSCloseSocketSessionException {
        final boolean exists = gameSessions.remove(gameSession);
        usersMap.remove(gameSession.getFirst().getId());
        usersMap.remove(gameSession.getSecond().getId());
        if (exists) {
            remotePointService.cutDownConnection(gameSession.getFirst().getId(), CloseStatus.SERVER_ERROR);
            remotePointService.cutDownConnection(gameSession.getSecond().getId(), CloseStatus.SERVER_ERROR);
        }
    }

    @Override
    public GameSession startGame(UserProfile first, UserProfile second) {
        final GameSession gameSession = new GameSession(first, second);
        gameSessions.add(gameSession);
        usersMap.put(gameSession.getFirst().getId(), gameSession);
        usersMap.put(gameSession.getSecond().getId(), gameSession);
        gameInitService.initGameFor(gameSession);
        return gameSession;
    }

}
