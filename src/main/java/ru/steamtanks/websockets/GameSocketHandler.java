package ru.steamtanks.websockets;

import org.jetbrains.annotations.NotNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import ru.steamtanks.exceptions.GameSessionService.GSSUserNotExist;
import ru.steamtanks.exceptions.RemotePointService.RPSSomeException;
import ru.steamtanks.main.RegistrationController;
import ru.steamtanks.mechanics.GameMechanics;
import ru.steamtanks.mechanics.services.implementation.GameSessionService;
import ru.steamtanks.services.implementation.AccountService;
import ru.steamtanks.mechanics.services.implementation.RemotePointService;
import ru.steamtanks.services.interfaces.Message;

import javax.naming.AuthenticationException;

public class GameSocketHandler implements WebSocketHandler {
    private final AccountService accountService;
    private final RemotePointService remotePointService;
    private @NotNull GameSessionService gameSessionService;


    public GameSocketHandler(AccountService accountService,
                             RemotePointService remotePointService,
                             @NotNull GameSessionService gameSessionService){
        this.accountService = accountService;
        this.remotePointService = remotePointService;
        this.gameSessionService = gameSessionService;
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
    public void afterConnectionEstablished(WebSocketSession webSocketSession)
            throws AuthenticationException, RPSSomeException {
        final Integer userId = (Integer) webSocketSession.getAttributes().get(RegistrationController.PRIMARY_KEY_TO_MAP);
        try {
            remotePointService.registerUser(userId, webSocketSession);
        }catch (RuntimeException e){
            System.out.println("error");
            e.getStackTrace();
        }
        Integer id = gameSessionService.startGame(accountService.getUser(userId), 2);
        final Message response = new Message("sessionId", id.toString());
        remotePointService.sendMessageToUser(userId, response);

//        pingService.refreshPing(userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession webSocketSession, CloseStatus closeStatus)
            throws Exception {
        final Integer userId = (Integer) webSocketSession.getAttributes().get(RegistrationController.PRIMARY_KEY_TO_MAP);
        try {
            gameSessionService.stopGame(accountService.getUser(userId));
        }catch (GSSUserNotExist e){
            e.getStackTrace();
        }
        remotePointService.removeUser(userId);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
