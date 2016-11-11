package ru.steamtanks.exceptions.AccountService;

import org.springframework.dao.DataAccessException;

public class ASUserExistException extends Exception {
    public ASUserExistException(String messege, DataAccessException cause){
        super(messege, cause);
    }
}
