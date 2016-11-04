package ru.steamtanks.services.interfaces;

import org.springframework.stereotype.Service;
import ru.steamtanks.exceptions.AccountService.ASDeleteUserException;
import ru.steamtanks.exceptions.AccountService.ASDetectUserException;
import ru.steamtanks.exceptions.AccountService.ASUserExistException;
import ru.steamtanks.models.UserProfile;

@Service
public interface AbstractAccountService {
    int addUser (String login, String password, String email) throws ASUserExistException, ASDetectUserException;

    void delUser(Integer id) throws ASDeleteUserException;

    UserProfile getUser(Integer id);

    UserProfile getUser(String login);

}
