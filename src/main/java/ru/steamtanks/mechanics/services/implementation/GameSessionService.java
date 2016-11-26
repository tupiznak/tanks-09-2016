package ru.steamtanks.mechanics.services.implementation;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.steamtanks.exceptions.GameSessionService.GSSUserNotExist;
import ru.steamtanks.exceptions.RemotePointService.RPSCloseSocketSessionException;
import ru.steamtanks.mechanics.GameSession;
import ru.steamtanks.mechanics.avatar.UserGameProfile;
import ru.steamtanks.mechanics.services.interfaces.AbstractGameSessionServise;
import ru.steamtanks.models.UserProfile;
import ru.steamtanks.services.implementation.AccountService;
import sun.reflect.generics.tree.Tree;

import java.util.*;

@Service
public class GameSessionService implements AbstractGameSessionServise {
    private final @NotNull Set<GameSession> gameSessions = new LinkedHashSet<>();

    private final @NotNull HashSet<GameSession> someEmptySessions = new HashSet<>();

    private final @NotNull RemotePointService remotePointService;

    private final @NotNull GameInitService gameInitService;

    private static final Logger LOGGER = Logger.getLogger(GameSessionService.class);

    public GameSessionService(@NotNull RemotePointService remotePointService, @NotNull GameInitService gameInitService) {
        this.remotePointService = remotePointService;
        this.gameInitService = gameInitService;
    }

    @Override
    public Set<GameSession> getSessions() {
        return gameSessions;
    }

    @Override
    public @NotNull GameSession getSessionForUser(@NotNull Integer userId) throws GSSUserNotExist {
        for (GameSession session : gameSessions) {
            final UserGameProfile userGameProfile = session.getSelf(userId);
            if (userGameProfile != null) {
                return session;
            }
        }
        LOGGER.error("it shouldn`t occur: userId not found");
        throw new GSSUserNotExist("WTF??!!", null);
    }

    @Override
    public boolean isPlaying(@NotNull Integer userId) {
        for (GameSession session : gameSessions) {
            final UserGameProfile userGameProfile = session.getSelf(userId);
            if (userGameProfile != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void notifyGameIsOver(@NotNull GameSession gameSession)
            throws RPSCloseSocketSessionException {
        final boolean exists = gameSessions.remove(gameSession);
        if (exists) {/*
            remotePointService.cutDownConnection
                    (gameSession.getFirst().getId(), CloseStatus.SERVER_ERROR);*/
        }
    }

    //// TODO: 11/24/16 threads
    @Override
    public Integer startGame(@NotNull UserProfile userProfile, Integer maxCountOfUsers) {
        if (someEmptySessions.isEmpty()) {
            final GameSession gameSession = new GameSession(maxCountOfUsers);
            someEmptySessions.add(gameSession);
            gameSessions.add(gameSession);
//            gameInitService.initGameFor(gameSession);
        }
        final GameSession session = someEmptySessions.iterator().next();//// TODO: 11/25/16 users >1
        if (session.getNowCountOfUsers() + 1 >= session.getMaxCountOfUsers()) {
            someEmptySessions.remove(session);
        }
        session.addUser(userProfile);
        return session.getIdGameSession();
    }

    @Override
    public void stopGame(@NotNull UserProfile userProfile) throws GSSUserNotExist {
        final GameSession gameSession = getSessionForUser(userProfile.getId());
        final UserGameProfile userGameProfile = gameSession.getSelf(userProfile.getId());
        gameSession.delUser(userGameProfile);
        if (gameSession.getNowCountOfUsers() == 0) {
            gameSessions.remove(gameSession);
            someEmptySessions.remove(gameSession);
            return;
        }
        if (gameSession.getNowCountOfUsers() == gameSession.getMaxCountOfUsers() - 1) {
            someEmptySessions.add(gameSession);
        }
    }

}
