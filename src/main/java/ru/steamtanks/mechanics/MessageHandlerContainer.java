package ru.steamtanks.mechanics;

import org.jetbrains.annotations.NotNull;
import ru.steamtanks.services.interfaces.Message;

public interface MessageHandlerContainer {

    void handle(@NotNull Message message, @NotNull Integer forUser) throws HandleException;

    <T> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler);
}
