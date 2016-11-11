package ru.steamtanks.services.implementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.steamtanks.exceptions.RemotePointService.RPSCloseSocketSessionException;
import ru.steamtanks.exceptions.RemotePointService.RPSSomeException;
import ru.steamtanks.services.interfaces.AbstractRemotePointService;
import ru.steamtanks.services.interfaces.Message;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RemotePointService implements AbstractRemotePointService {
    private Map<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void registerUser(@NotNull Integer userId, @NotNull WebSocketSession webSocketSession) {
        sessions.put(userId, webSocketSession);
    }

    @Override
    public boolean isConnected(@NotNull Integer userId) {
        return sessions.containsKey(userId) && sessions.get(userId).isOpen();
    }

    @Override
    public void removeUser(@NotNull Integer userId) {
        sessions.remove(userId);
    }

    @Override
    public void cutDownConnection(@NotNull Integer userId, @NotNull CloseStatus closeStatus)
            throws RPSCloseSocketSessionException {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession != null && webSocketSession.isOpen()) {
            try {
                webSocketSession.close(closeStatus);
            } catch (IOException e) {
                throw new RPSCloseSocketSessionException
                        ("Can't close socket session to user <"+ userId +">:", e);
            }
        }
    }

    @Override
    public void sendMessageToUser(@NotNull Integer userId, @NotNull Message message)
            throws RPSSomeException {
        final WebSocketSession webSocketSession = sessions.get(userId);
        if (webSocketSession == null) {
            throw new RPSSomeException("no game websocket for user " + userId, null);
        }
        if (!webSocketSession.isOpen()) {
            throw new RPSSomeException("session is closed or not exsists", null);
        }
        try {
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (JsonProcessingException e){
            throw new RPSSomeException("Cant serialize JSON",e);
        } catch (IOException e) {
            throw new RPSSomeException("Unnable to send message", e);
        }
    }
}
