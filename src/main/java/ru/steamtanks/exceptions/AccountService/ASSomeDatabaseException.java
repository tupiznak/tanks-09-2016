package ru.steamtanks.exceptions.AccountService;

import org.springframework.dao.DataAccessException;

public class ASSomeDatabaseException extends Exception {
    public ASSomeDatabaseException(String messege, DataAccessException cause){
        super(messege, cause);
    }
}
