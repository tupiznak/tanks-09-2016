package ru.steamtanks.services.interfaces;

import org.jetbrains.annotations.NotNull;

public class Message {
    private String type;
    private String content;

    public String getType() {
        return type;
    }
    public String getContent() {
        return content;
    }

    public Message() {
    }

    public Message(@NotNull String type, @NotNull String content) {
        this.type = type;
        this.content = content;
    }

    public Message(@NotNull Class clazz, @NotNull String content) {
        this(clazz.getName(), content);
    }
}
