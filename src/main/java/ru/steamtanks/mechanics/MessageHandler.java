package ru.steamtanks.mechanics;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.jetbrains.annotations.NotNull;
import ru.steamtanks.services.interfaces.Message;

import java.io.IOException;

public abstract class MessageHandler<T> {
    private final @NotNull Class<T> clazz;

    public MessageHandler(@NotNull Class<T> clazz) {
        this.clazz = clazz;
    }

    @SuppressWarnings("OverlyBroadCatchBlock")
    public void handleMessage(@NotNull Message message, @NotNull Integer forUser)
            throws HandleException {
        try {
            final Object data = new ObjectMapper().readValue(message.getContent(), clazz);

            //noinspection ConstantConditions
            handle(clazz.cast(data), forUser);
        } catch (IOException | ClassCastException ex) {
            throw new HandleException("Can't read incoming message of type " + message.getType() + " with content: " + message.getContent(), ex);
        }
    }

    public abstract void handle(@NotNull T message, @NotNull Integer forUser) throws HandleException;
}
