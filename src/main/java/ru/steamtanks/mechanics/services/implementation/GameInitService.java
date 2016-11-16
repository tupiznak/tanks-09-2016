package ru.steamtanks.mechanics.services.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import ru.steamtanks.exceptions.RemotePointService.RPSCloseSocketSessionException;
import ru.steamtanks.exceptions.RemotePointService.RPSSomeException;
import ru.steamtanks.mechanics.Config;
import ru.steamtanks.mechanics.avatar.PositionPart;
import ru.steamtanks.mechanics.avatar.UserGameProfile;
import ru.steamtanks.mechanics.base.Coords;
import ru.steamtanks.mechanics.base.ServerPlayerSnap;
import ru.steamtanks.mechanics.GameSession;
import ru.steamtanks.mechanics.requests.InitGame;
import ru.steamtanks.services.interfaces.Message;

import java.io.IOException;
import java.util.*;

@Service
public class GameInitService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ServerSnapshotService.class);

    private final @NotNull RemotePointService remotePointService;

    private final @NotNull ObjectMapper objectMapper = new ObjectMapper();

    public GameInitService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void initGameFor(@NotNull GameSession gameSession) {
        gameSession.getFirst().getSquare().claimPart(PositionPart.class).setBody(new Coords(0.0f, 0.0f));
        gameSession.getSecond().getSquare().claimPart(PositionPart.class).setBody(
                new Coords(Config.PLAYGROUND_WIDTH - Config.SQUARE_SIZE,
                        Config.PLAYGROUND_HEIGHT - Config.SQUARE_SIZE)
        );
        final Collection<UserGameProfile> players = new ArrayList<>();
        players.add(gameSession.getFirst());
        players.add(gameSession.getSecond());
        for (UserGameProfile player : players) {
            final InitGame.Request initMessage = createInitMessageFor(gameSession, player.getId());
            try {
                final Message message = new Message(InitGame.Request.class.getName(),
                        objectMapper.writeValueAsString(initMessage));
                remotePointService.sendMessageToUser(player.getId(), message);
            } catch (IOException e) {
                players.forEach(playerToCutOff -> {
                    try {
                        remotePointService.cutDownConnection(playerToCutOff.getId(),
                                CloseStatus.SERVER_ERROR);
                    } catch (RPSCloseSocketSessionException e1) {
                        e1.printStackTrace();
                    }
                });
                LOGGER.error("Unnable to start a game", e);
            } catch (RPSSomeException e) {
                e.printStackTrace();
            }
        }
    }

    private InitGame.Request createInitMessageFor(@NotNull GameSession gameSession, @NotNull Integer userId) {
        final UserGameProfile self = gameSession.getSelf(userId);
        final InitGame.Request initGameMessage = new InitGame.Request();

        final List<ServerPlayerSnap> playerSnaps = new ArrayList<>();
        final Map<Integer, String> names = new HashMap<>();
        final Map<Integer, String> colors = new HashMap<>();
        final Map<Integer, String> gunColors = new HashMap<>();

        colors.put(userId, Config.SELF_COLOR);
        gunColors.put(userId, Config.SELF_GUN_COLOR);
        colors.put(gameSession.getEnemy(self).getId(), Config.ENEMY_COLOR);
        gunColors.put(gameSession.getEnemy(self).getId(), Config.ENEMY_GUN_COLOR);

        final Collection<UserGameProfile> players = new ArrayList<>();
        players.add(gameSession.getFirst());
        players.add(gameSession.getSecond());
        for (UserGameProfile player : players) {
            playerSnaps.add(player.generateSnap());
            names.put(player.getId(), player.getUserProfile().getLogin());
        }

        initGameMessage.setSelf(userId);
        initGameMessage.setSelfSquareId(gameSession.getSelf(userId).getSquare().getId());
        initGameMessage.setColors(colors);
        initGameMessage.setGunColors(gunColors);
        initGameMessage.setNames(names);
        initGameMessage.setPlayers(playerSnaps);
        return initGameMessage;
    }
}
