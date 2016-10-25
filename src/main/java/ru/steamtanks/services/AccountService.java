package ru.steamtanks.services;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.steamtanks.models.UserProfile;

import java.util.List;

@Service
@Transactional
public class AccountService {
    private final JdbcTemplate template;
    private static final String TABLE_USERS = "users";

    public AccountService(JdbcTemplate template) {
        this.template = template;
    }

    public Integer addUser(String login, String password, String email) {
        try {
            template.update(
                    "insert into "+TABLE_USERS+" (login,password,email) values(?,?,?)",
                    login, password, email);
        }
        catch (Exception e){
            return -1;
        }
        Integer id = template.queryForObject(
                "select id from "+TABLE_USERS+" where login=?",Integer.class,login
        );

        return id;
    }

    public Boolean delUser(Integer id) {
        try{
        template.update(
                "delete from "+TABLE_USERS+" where id=?",
                id);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public UserProfile getUser(Integer id) {

        final RowMapper<UserProfile> userProfileRowMapper =
                (res, rowNum) -> new UserProfile(
                        res.getString(1),
                        res.getString(2),
                        res.getString(3)
                        );

        List<UserProfile> value = template.query(
                "select login, password, email from "+TABLE_USERS+" where id=?",
                userProfileRowMapper, id);
        if(value.isEmpty())
            return null;

        UserProfile userProfile = new UserProfile(
                value.get(0).getLogin(),
                value.get(0).getPassword(),
                value.get(0).getEmail()
        );

        return userProfile;
    }

    public UserProfile getUser(String login) {

        final RowMapper<UserProfile> userProfileRowMapper =
                (res, rowNum) -> new UserProfile(
                        login,
                        res.getString(1),
                        res.getString(2),
                        res.getInt(3)
                );

        List<UserProfile> value = template.query(
                "select password, email, id from "+TABLE_USERS+" where login=?",
                userProfileRowMapper, login);
        if(value.isEmpty())
            return null;

        UserProfile userProfile = new UserProfile(
                value.get(0).getLogin(),
                value.get(0).getPassword(),
                value.get(0).getEmail(),
                value.get(0).getId()
        );
        return userProfile;
    }
    /*
    private String validation(String string){
        if(StringUtils.isEmpty(string))
            return null;
        string.replaceAll("<", "\"<");
        return string;
    }
        if(StringUtils.isEmpty(validation(login))
            ||StringUtils.isEmpty(validation(password))
            ||StringUtils.isEmpty(validation(email)))
            return ;
*/
}
