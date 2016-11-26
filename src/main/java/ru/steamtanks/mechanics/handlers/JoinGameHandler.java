package ru.steamtanks.mechanics.handlers;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.steamtanks.mechanics.GameMechanics;
import ru.steamtanks.mechanics.HandleException;
import ru.steamtanks.mechanics.MessageHandler;
import ru.steamtanks.mechanics.MessageHandlerContainer;
import ru.steamtanks.mechanics.requests.JoinGame;

import javax.annotation.PostConstruct;

@Component
public class JoinGameHandler extends MessageHandler<JoinGame.Request> {
    private @NotNull GameMechanics gameMechanics;
    private @NotNull MessageHandlerContainer messageHandlerContainer;

    public JoinGameHandler(@NotNull GameMechanics gameMechanics, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(JoinGame.Request.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(JoinGame.Request.class, this);
    }

    @Override
    public void handle(@NotNull JoinGame.Request message, @NotNull Integer forUser)
            throws HandleException {
        System.out.println(forUser);
//        gameMechanics.addUser(forUser);
    }
}
