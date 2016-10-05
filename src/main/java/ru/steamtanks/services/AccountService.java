package ru.steamtanks.services;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.steamtanks.models.UserProfile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {
    private Map<String, UserProfile> userNameToUser = new ConcurrentHashMap< >();

    public UserProfile addUser(String login, String password, String email){
        final UserProfile userProfile = new UserProfile(login, password, email);
        userNameToUser.put(login,userProfile);
        return userProfile;
    }

    public Boolean delUser(String login){
        if(StringUtils.isEmpty(login))
            return false;
        userNameToUser.remove(login);
        return true;
    }

    public UserProfile getUser(String login){
        if(StringUtils.isEmpty(login))
            return null;
        return userNameToUser.get(login);
    }
}
