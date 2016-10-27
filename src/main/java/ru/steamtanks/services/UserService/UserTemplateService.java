package ru.steamtanks.services.UserService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.steamtanks.models.UserProfile;

/**
 * Created by nikita on 25.10.16.
 */
@Service
@Transactional
public class UserTemplateService implements UserServiceI {

    private JdbcTemplate template = new JdbcTemplate();

    public UserTemplateService(JdbcTemplate template) {
        this.template = template;
    }

    private static final RowMapper<UserProfile> UserProfile_MAPPER =
            (res, rowNum) -> new UserProfile(res.getString(1), res.getString(2),res.getString(3) );

    @Override
    public UserProfile addUser(String login, String password, String email) {

        template.update("insert into User(`email`, `login`, `password`) " +
                        "  VALUES (email = ?, 'login = ?, password = ?)",
                email, login, password);

        return new UserProfile(login,password, email);
    }

    @Override
    public Boolean delUser(String login){
        if(StringUtils.isEmpty(login))
            return false;
        template.update("DELETE FROM  User WHERE login=?;" , login);
        return true;
    }

    @Override
    public UserProfile getUser(String login){
        return template.query("select * from User Where login = ?", UserProfile_MAPPER, login).get(0);
    }
}
