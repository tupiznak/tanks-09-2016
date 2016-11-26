package ru.steamtanks.mechanics.handlers;

import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.steamtanks.mechanics.GameMechanics;
import ru.steamtanks.mechanics.HandleException;
import ru.steamtanks.mechanics.MessageHandler;
import ru.steamtanks.mechanics.MessageHandlerContainer;
import ru.steamtanks.mechanics.base.ClientSnap;

import javax.annotation.PostConstruct;

@Component
public class ClientSnapHandler extends MessageHandler<ClientSnap> {
    private @NotNull GameMechanics gameMechanics;
    private @NotNull MessageHandlerContainer messageHandlerContainer;

    public ClientSnapHandler(@NotNull GameMechanics gameMechanics, @NotNull MessageHandlerContainer messageHandlerContainer) {
        super(ClientSnap.class);
        this.gameMechanics = gameMechanics;
        this.messageHandlerContainer = messageHandlerContainer;
    }

    @PostConstruct
    private void init() {
        messageHandlerContainer.registerHandler(ClientSnap.class, this);
    }

    @Override
    public void handle(@NotNull ClientSnap message, @NotNull Integer forUser) throws HandleException {
        gameMechanics.addClientSnapshot(forUser, message);
    }
}
