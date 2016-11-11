package ru.steamtanks.services.interfaces;

import org.springframework.stereotype.Service;
import ru.steamtanks.exceptions.AccountService.ASSomeDatabaseException;
import ru.steamtanks.exceptions.AccountService.ASUserExistException;
import ru.steamtanks.models.UserProfile;

@Service
public interface AbstractAccountService {
    int addUser(String login, String password, String email)
            throws ASUserExistException, ASSomeDatabaseException;

    void delUser(Integer id) throws ASSomeDatabaseException;

    UserProfile getUser(Integer id);

    UserProfile getUser(String login);

}
