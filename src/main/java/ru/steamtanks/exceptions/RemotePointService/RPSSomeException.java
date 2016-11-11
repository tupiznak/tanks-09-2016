package ru.steamtanks.exceptions.RemotePointService;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class RPSSomeException extends Exception {
    public RPSSomeException(String message, @Nullable Exception cause){
        super(message, cause);
    }
}
