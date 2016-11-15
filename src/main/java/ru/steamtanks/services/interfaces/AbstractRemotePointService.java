package ru.steamtanks.services.interfaces;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import ru.steamtanks.exceptions.RemotePointService.RPSCloseSocketSessionException;
import ru.steamtanks.exceptions.RemotePointService.RPSSomeException;

@Service
public interface AbstractRemotePointService {
    void registerUser(@NotNull Integer userId, @NotNull WebSocketSession webSocketSession);

    boolean isConnected(@NotNull Integer userId);

    void removeUser(@NotNull Integer userId);

    void cutDownConnection(@NotNull Integer userId, @NotNull CloseStatus closeStatus)
            throws RPSCloseSocketSessionException;

    void sendMessageToUser(@NotNull Integer userId, @NotNull Message message)
            throws RPSSomeException;

    Integer countOfUsers();
}
