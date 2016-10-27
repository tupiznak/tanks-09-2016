package ru.steamtanks.services.UserService;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.steamtanks.models.UserProfile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by nikita on 25.10.16.
 */
@Service
public class DummyUserService implements UserServiceI {
    private Map<String, UserProfile> userNameToUser = new ConcurrentHashMap< >();

    @Override
    public UserProfile addUser(String login, String password, String email){
        final UserProfile userProfile = new UserProfile(login, password, email);
        userNameToUser.put(login,userProfile);
        return userProfile;
    }

    @Override
    public Boolean delUser(String login){
        if(StringUtils.isEmpty(login))
            return false;
        userNameToUser.remove(login);
        return true;
    }

    @Override
    public UserProfile getUser(String login){
        if(StringUtils.isEmpty(login))
            return null;
        return userNameToUser.get(login);
    }
}
