package ru.steamtanks.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.steamtanks.exceptions.AccountService.ASSomeDatabaseException;
import ru.steamtanks.exceptions.AccountService.ASUserExistException;
import ru.steamtanks.models.UserProfile;
import ru.steamtanks.services.implementation.AccountService;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
@CrossOrigin()
public class RegistrationController {

    //add LOGGER

    private final AccountService accountService;
    private final HttpSession httpSession;

    private static final String PRIMARY_KEY_TO_MAP = "iduser";

    @Autowired
    public RegistrationController(AccountService accountService,
                                  HttpSession httpSession) {
        this.accountService = accountService;
        this.httpSession = httpSession;
    }

    //user
    @RequestMapping(path = "/api/user", method = RequestMethod.POST)
    public ResponseEntity userAdd(@RequestBody RegistrationRequest body) {
        final String login = body.getLogin();
        final String email = body.getEmail();
        final String password = body.getPassword();

        if (StringUtils.isEmpty(login) ||
                StringUtils.isEmpty(email) ||
                StringUtils.isEmpty(password))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");

        final int id;
        try {
            id = accountService.addUser(login, password, email);
        } catch (ASUserExistException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
        } catch (ASSomeDatabaseException e) {
            //// TODO: 11/11/16 need add to api "ServerError"
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
        }

        httpSession.setAttribute(PRIMARY_KEY_TO_MAP, id);
        return ResponseEntity.ok().header("Set-Cookie", "id=" + id).body("{}");
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.DELETE)
    public ResponseEntity userDel() {
        final Integer id = (Integer) httpSession.getAttribute(PRIMARY_KEY_TO_MAP);

        try {
            accountService.delUser(id);
        } catch (ASSomeDatabaseException e) {
            //// TODO: 11/11/16 need add to api "ServerError"
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
        }

        sessionDel();
        return ResponseEntity.ok("{}");
    }

    //session
    @RequestMapping(path = "/api/session", method = RequestMethod.GET)
    public ResponseEntity sessionGet() {
        final UserProfile existingUser = accountService.getUser((Integer) httpSession.getAttribute(PRIMARY_KEY_TO_MAP));
        if (existingUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");

        return ResponseEntity.ok(existingUser);
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.POST)
    public ResponseEntity sessionLogin(@RequestBody RegistrationRequest body) {
        final String login = body.getLogin();
        final String password = body.getPassword();

        if (StringUtils.isEmpty(login) ||
                StringUtils.isEmpty(password))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");

        final UserProfile existingUser = accountService.getUser(login);
        if (existingUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");

        if (!Objects.equals(existingUser.getPassword(), password))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");

        httpSession.setAttribute(PRIMARY_KEY_TO_MAP, existingUser.getId());

        return ResponseEntity.ok(existingUser);
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.DELETE)
    public ResponseEntity sessionDel() {
        final Integer id = (Integer) httpSession.getAttribute(PRIMARY_KEY_TO_MAP);

        if (StringUtils.isEmpty(id))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");

        httpSession.invalidate();

        return ResponseEntity.ok("{}");
    }

    private static final class RegistrationRequest {
        private String login;
        private String email;
        private String password;
        private String newPassword;

        public String getLogin() {
            return login;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }

        public String getNewPassword() {
            return newPassword;
        }
    }
}