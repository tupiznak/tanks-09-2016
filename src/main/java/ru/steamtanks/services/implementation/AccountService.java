package ru.steamtanks.services.implementation;

import org.apache.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.steamtanks.exceptions.AccountService.ASSomeDatabaseException;
import ru.steamtanks.exceptions.AccountService.ASUserExistException;
import ru.steamtanks.models.UserProfile;
import ru.steamtanks.services.interfaces.AbstractAccountService;

import java.util.List;

@Service
@Transactional
public class AccountService implements AbstractAccountService {
    private final JdbcTemplate template;

    private static final Logger LOGGER = Logger.getLogger(AccountService.class);

    public static String getTableUsers() {
        return TABLE_USERS;
    }

    private static final String TABLE_USERS = "users";

    public AccountService(JdbcTemplate template) {
        this.template = template;
    }

    @Override
    public int addUser(String login, String password, String email)
            throws ASUserExistException, ASSomeDatabaseException {
        try {//// TODO: 11/11/16 need go to JDBCT Spring
            template.update(
                    "INSERT INTO " + TABLE_USERS + " (login,password,email) VALUES(?,?,?)",
                    login, password, email);

            return template.queryForObject(
                    "SELECT id FROM " + TABLE_USERS + " WHERE login=?", Integer.class, login
            );
        } catch (DuplicateKeyException e) {
            if (LOGGER.isDebugEnabled())
                LOGGER.debug("Exception user exist", e);
            LOGGER.warn("User exist");
            throw new ASUserExistException("User exist", e);
        } catch (DataAccessException e) {
            LOGGER.error("Exception in add user", e);
            throw new ASSomeDatabaseException("Exception in add user", e);
        }
    }

    @Override
    public void delUser(Integer id) throws ASSomeDatabaseException {
        try {
            template.update(
                    "DELETE FROM " + TABLE_USERS + " WHERE id=?",
                    id);
        } catch (DataAccessException e) {
            LOGGER.error("Exception in delete user", e);
            throw new ASSomeDatabaseException("Exception in delete user", e);
        }
    }

    @Override
    public @Nullable UserProfile getUser(Integer id) {
        final RowMapper<UserProfile> userProfileRowMapper =
                (res, rowNum) -> new UserProfile(
                        res.getString(1),
                        res.getString(2),
                        res.getString(3)
                );

        final List<UserProfile> value = template.query(
                "SELECT login, password, email FROM " + TABLE_USERS + " WHERE id=?",
                userProfileRowMapper, id);
        if (value.isEmpty())
            return null;

        return new UserProfile(
                value.get(0).getLogin(),
                value.get(0).getPassword(),
                value.get(0).getEmail()
        );
    }

    @Override
    public @Nullable UserProfile getUser(String login) {

        final RowMapper<UserProfile> userProfileRowMapper =
                (res, rowNum) -> new UserProfile(
                        login,
                        res.getString(1),
                        res.getString(2),
                        res.getInt(3)
                );

        final List<UserProfile> value = template.query(
                "SELECT password, email, id FROM " + TABLE_USERS + " WHERE login=?",
                userProfileRowMapper, login);
        if (value.isEmpty())
            return null;

        return new UserProfile(
                value.get(0).getLogin(),
                value.get(0).getPassword(),
                value.get(0).getEmail(),
                value.get(0).getId()
        );
    }
}
