package ru.steamtanks.exceptions.RemotePointService;

import java.io.IOException;

public class RPSCloseSocketSessionException extends Exception{
    public RPSCloseSocketSessionException(String message, IOException cause){
        super(message, cause);
    }
}
