package ru.steamtanks.mechanics.services.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.steamtanks.exceptions.RemotePointService.RPSSomeException;
import ru.steamtanks.mechanics.GameSession;
import ru.steamtanks.mechanics.avatar.UserGameProfile;
import ru.steamtanks.mechanics.base.ServerPlayerSnap;
import ru.steamtanks.mechanics.base.ServerSnap;
import ru.steamtanks.services.interfaces.Message;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class ServerSnapshotService {
    private final @NotNull RemotePointService remotePointService;

    private final @NotNull ObjectMapper objectMapper = new ObjectMapper();

    public ServerSnapshotService(@NotNull RemotePointService remotePointService) {
        this.remotePointService = remotePointService;
    }

    public void sendSnapshotsFor(@NotNull GameSession gameSession, long frameTime) {
        final Collection<UserGameProfile> players = new ArrayList<>();
        players.add(gameSession.getFirst());
        players.add(gameSession.getSecond());
        final List<ServerPlayerSnap> playersSnaps = new ArrayList<>();
        for (UserGameProfile player : players) {
            playersSnaps.add(player.generateSnap());
        }
        final ServerSnap snap = new ServerSnap();

        snap.setPlayers(playersSnaps);
        snap.setServerFrameTime(frameTime);
        try {
            final Message message = new Message(ServerSnap.class.getName(), objectMapper.writeValueAsString(snap));
            for (UserGameProfile player : players) {
                remotePointService.sendMessageToUser(player.getId(), message);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Failed sending snapshot", ex);
        } catch (RPSSomeException e) {
            e.printStackTrace();
        }

    }
}
