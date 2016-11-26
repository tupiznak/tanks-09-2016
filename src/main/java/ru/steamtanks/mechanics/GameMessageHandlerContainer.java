package ru.steamtanks.mechanics;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.steamtanks.services.interfaces.Message;

import java.util.HashMap;
import java.util.Map;

@Service
public class GameMessageHandlerContainer implements MessageHandlerContainer {
    @SuppressWarnings("ConstantConditions")
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(GameMessageHandlerContainer.class);
    final Map<Class<?>, MessageHandler<?>> handlerMap = new HashMap<>();

    @Override
    public void handle(@NotNull Message message, @NotNull Integer forUser) throws HandleException {

        final Class clazz;
        try {
            clazz = Class.forName(message.getType());
        } catch (ClassNotFoundException e) {
            throw new HandleException("Can't handle message of " + message.getType() + " type", e);
        }
        final MessageHandler<?> messageHandler = handlerMap.get(clazz);
        if (messageHandler == null) {
            throw new HandleException("no handler for message of " + message.getType() + " type");
        }
        messageHandler.handleMessage(message, forUser);
        LOGGER.debug("message handled: type =[" + message.getType() + "], content=[" + message.getContent() + ']');
    }

    @Override
    public <T> void registerHandler(@NotNull Class<T> clazz, MessageHandler<T> handler) {
        handlerMap.put(clazz, handler);
    }


}
