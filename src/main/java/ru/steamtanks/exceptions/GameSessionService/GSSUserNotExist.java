package ru.steamtanks.exceptions.GameSessionService;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class GSSUserNotExist extends Exception{
    public GSSUserNotExist(String message, @Nullable IOException cause) {
        super(message, cause);
    }
}
