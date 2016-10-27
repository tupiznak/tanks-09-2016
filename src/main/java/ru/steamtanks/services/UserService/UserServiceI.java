package ru.steamtanks.services.UserService;

import org.springframework.util.StringUtils;
import ru.steamtanks.models.UserProfile;

/**
 * Created by nikita on 25.10.16.
 */
public interface UserServiceI {

    public UserProfile addUser(String login, String password, String email);

    public Boolean delUser(String login);

    public UserProfile getUser(String login);
}
