package ru.steamtanks.services.implementation;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.steamtanks.exceptions.AccountService.ASDeleteUserException;
import ru.steamtanks.exceptions.AccountService.ASDetectUserException;
import ru.steamtanks.exceptions.AccountService.ASUserExistException;
import ru.steamtanks.models.UserProfile;
import ru.steamtanks.services.interfaces.AbstractAccountService;

import java.util.List;

@Service
@Transactional
public class AccountService implements AbstractAccountService {
    private final JdbcTemplate template;

    public static String getTableUsers() {
        return TABLE_USERS;
    }

    private static final String TABLE_USERS = "users";

    public AccountService(JdbcTemplate template) {
        this.template = template;
    }

    public int addUser(String login, String password, String email)
            throws ASUserExistException, ASDetectUserException {
        try {
            template.update(
                    "insert into "+TABLE_USERS+" (login,password,email) values(?,?,?)",
                    login, password, email);
        }
        catch (DataAccessException e){
            throw new ASUserExistException();
        }
        Integer id;
        try {
            id = template.queryForObject(
                    "select id from "+TABLE_USERS+" where login=?",Integer.class,login
            );
        }catch (DataAccessException e){
            throw new ASDetectUserException();
        }

        return id;
    }

    public void delUser(Integer id) throws ASDeleteUserException {
        try{
        template.update(
                "delete from "+TABLE_USERS+" where id=?",
                id);
        }
        catch (DataAccessException e){
            throw new ASDeleteUserException();
        }
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
