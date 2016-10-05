package ru.steamtanks.main;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.steamtanks.models.UserProfile;
import ru.steamtanks.services.AccountService;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
@CrossOrigin()
public class RegistrationController {

    final private AccountService accountService;
    final private HttpSession httpSession;

    final String primaryKeyToMap = "username";

    @Autowired
    public RegistrationController(AccountService accountService,
                                  HttpSession httpSession) {
        this.accountService = accountService;
        this.httpSession = httpSession;
    }

    //user
    @RequestMapping(path = "/api/user", method = RequestMethod.POST)
    public ResponseEntity userAdd(@RequestBody RegistrationRequest body) {
        String login = body.getLogin();
        String email = body.getEmail();
        String password = body.getPassword();

        //validation on front

        final UserProfile existingUser = accountService.getUser(login);
        if (existingUser != null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");

        accountService.addUser(login, password, email);

        httpSession.setAttribute(primaryKeyToMap, login);

        return ResponseEntity.ok("{}");
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.DELETE)
    public ResponseEntity userDel() {
        String login = (String) httpSession.getAttribute(primaryKeyToMap);

        if (StringUtils.isEmpty(login))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");

        accountService.delUser(login);
        sessionDel();

        return ResponseEntity.ok("{}");
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.PUT)
    public ResponseEntity userChange(@RequestBody RegistrationRequest body) {
        String email = body.getEmail();
        String password = body.getPassword();
        String newPassword = body.getNewPassword();

        String login = (String) httpSession.getAttribute(primaryKeyToMap);

        final UserProfile existingUser = accountService.getUser(login);
        if (existingUser == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");

        if (!Objects.equals(password, existingUser.getPassword()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
        if (!StringUtils.isEmpty(newPassword))
            existingUser.setPassword(password);
        if (!StringUtils.isEmpty(email))
            existingUser.setEmail(email);

        return ResponseEntity.ok("{}");
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.GET)
    public ResponseEntity userGet() {
        String login = (String) httpSession.getAttribute(primaryKeyToMap);

        if (StringUtils.isEmpty(login))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");

        final UserProfile existingUser = accountService.getUser(login);
        if (existingUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");

        return ResponseEntity.ok(existingUser);
    }

    //session
    @RequestMapping(path = "/api/session", method = RequestMethod.GET)
    public ResponseEntity sessionGet() {
        final UserProfile existingUser = accountService.getUser((String) httpSession.getAttribute(primaryKeyToMap));
        if (existingUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");

        return ResponseEntity.ok(existingUser);
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.POST)
    public ResponseEntity sessionLogin(@RequestBody RegistrationRequest body) throws JsonProcessingException {
        String login = body.getLogin();
        String password = body.getPassword();

        //validation on front

        final UserProfile existingUser = accountService.getUser(login);
        if (existingUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");

        if (!Objects.equals(existingUser.getPassword(), password))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");

        httpSession.setAttribute(primaryKeyToMap, login);

        return ResponseEntity.ok(existingUser);
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.DELETE)
    public ResponseEntity sessionDel() {
        String login = (String) httpSession.getAttribute(primaryKeyToMap);

        if (StringUtils.isEmpty(login))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");

        httpSession.invalidate();

        return ResponseEntity.ok("{}");
    }

    private static final class RegistrationRequest {
        private String login;
        private String email;
        private String password;
        private String newPassword;

        public String getLogin() { return login; }

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
