package ru.steamtanks.services;

import org.springframework.stereotype.Service;
import ru.steamtanks.models.UserProfile;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private Map<String, UserProfile> userNameToUser = new HashMap<>();

    public UserProfile addUser(String login, String password, String email){
        final UserProfile userProfile = new UserProfile(login, password, email);
        userNameToUser.put(login,userProfile);
        return userProfile;
    }

    public Boolean delUser(String login){
        userNameToUser.remove(login);
        return true;
    }

    public UserProfile getUser(String login){
        return userNameToUser.get(login);
    }
}
