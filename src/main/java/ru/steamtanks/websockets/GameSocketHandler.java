package ru.steamtanks.websockets;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.steamtanks.services.implementation.AccountService;
import ru.steamtanks.services.implementation.RemotePointService;

import javax.naming.AuthenticationException;
import javax.persistence.criteria.CriteriaBuilder;

public class GameSocketHandler implements WebSocketHandler {
    private final AccountService accountService;
    private final RemotePointService remotePointService;

    public GameSocketHandler(AccountService accountService,
                             RemotePointService remotePointService){
        this.accountService = accountService;
        this.remotePointService = remotePointService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws AuthenticationException {
        final Integer userId = (Integer) webSocketSession.getAttributes().get("userId");

        remotePointService.registerUser(userId, webSocketSession);
//        pingService.refreshPing(userId);
    }

    @Override
    public void handleMessage(WebSocketSession webSocketSession, WebSocketMessage<?> webSocketMessage)
            throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession webSocketSession, Throwable throwable)
            throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus)
            throws Exception {

    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
