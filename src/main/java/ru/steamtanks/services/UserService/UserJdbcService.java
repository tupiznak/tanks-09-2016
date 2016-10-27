package ru.steamtanks.services.UserService;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import ru.steamtanks.models.UserProfile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by nikita on 25.10.16.
 */
@Service
public class UserJdbcService implements UserServiceI {
    private final DataSource ds;

    public UserJdbcService(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public UserProfile addUser(String login, String password, String email){
        Connection con = DataSourceUtils.getConnection(ds);
        String insertTableSQL = "INSERT INTO User"
                + "(`email`, `login`, `password`) VALUES"
                + "(?,?,?)";
        try (PreparedStatement pst = con.prepareStatement(insertTableSQL)) {
            pst.setString(1, email);
            pst.setString(2, login);
            pst.setString(3, password);
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new UserProfile(login, password, email);
    }

    @Override
    public Boolean delUser(String login){
        if(StringUtils.isEmpty(login))
            return false;

        Connection con = DataSourceUtils.getConnection(ds);
        String insertTableSQL = "DELETE FROM  User WHERE login=?;";
        try ( PreparedStatement pst = con.prepareStatement(insertTableSQL) ) {
            pst.setString(1, login);
            pst.executeUpdate();
        }catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public UserProfile getUser(String login){
        Connection con = DataSourceUtils.getConnection(ds);
        String insertTableSQL = "select login, password, email from User where login=?";
        try (PreparedStatement pst = con.prepareStatement(insertTableSQL)) {
            pst.setString(1, login);
            try (ResultSet res = pst.executeQuery()) {
                Assert.isTrue(res.next());
                String login_ = res.getString(1);
                return new UserProfile(res.getString(1), res.getString(2), res.getString(3));
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
