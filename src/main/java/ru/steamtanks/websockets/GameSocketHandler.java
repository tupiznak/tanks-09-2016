package ru.steamtanks.websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.steamtanks.exceptions.RemotePointService.RPSSomeException;
import ru.steamtanks.main.RegistrationController;
import ru.steamtanks.services.implementation.AccountService;
import ru.steamtanks.services.implementation.RemotePointService;
import ru.steamtanks.services.interfaces.Message;

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
    public void afterConnectionEstablished(WebSocketSession webSocketSession)
            throws AuthenticationException, RPSSomeException {
        final Integer userId = (Integer) webSocketSession.getAttributes().get(RegistrationController.PRIMARY_KEY_TO_MAP);
        try {
            remotePointService.registerUser(userId, webSocketSession);
        }catch (RuntimeException e){
            System.out.println("error");
            e.getStackTrace();
        }
        final Message response = new Message("count_of_users", remotePointService.countOfUsers().toString());
        remotePointService.sendMessageToUser(userId, response);
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
        final Integer userId = (Integer) webSocketSession.getAttributes().get(RegistrationController.PRIMARY_KEY_TO_MAP);
        remotePointService.removeUser(userId);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
