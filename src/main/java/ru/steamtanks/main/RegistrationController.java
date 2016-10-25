package ru.steamtanks.main;

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
        String login = body.getLogin();
        String email = body.getEmail();
        String password = body.getPassword();

        if (    StringUtils.isEmpty(login)||
                StringUtils.isEmpty(email)||
                StringUtils.isEmpty(password))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");

        Integer id = accountService.addUser(login, password, email);

        if(id == -1)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");

        httpSession.setAttribute(PRIMARY_KEY_TO_MAP, id);
        return ResponseEntity.ok().header("Set-Cookie", "id=" + id).body("{}");

    }

    @RequestMapping(path = "/api/user", method = RequestMethod.DELETE)
    public ResponseEntity userDel() {
        Integer id = (Integer) httpSession.getAttribute(PRIMARY_KEY_TO_MAP);

        Boolean isDel = accountService.delUser(id);
        sessionDel();

        if(!isDel)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");

        return ResponseEntity.ok("{}");
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.PUT)
    public ResponseEntity userChange(@RequestBody RegistrationRequest body) {
        /*String email = body.getEmail();
        String password = body.getPassword();
        String newPassword = body.getNewPassword();

        String login = (String) httpSession.getAttribute(PRIMARY_KEY_TO_MAP);

        final UserProfile existingUser = accountService.getUser(login);
        if (existingUser == null)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");

        if (!Objects.equals(password, existingUser.getPassword()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
        if (!StringUtils.isEmpty(newPassword))
            existingUser.setPassword(password);
        if (!StringUtils.isEmpty(email))
            existingUser.setEmail(email);
*/
        return ResponseEntity.ok("{}");
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.GET)
    public ResponseEntity userGet() {
        Integer id = (Integer) httpSession.getAttribute(PRIMARY_KEY_TO_MAP);
        System.out.println(id);

        if (StringUtils.isEmpty(id))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");

        final UserProfile existingUser = accountService.getUser(id);
        if (existingUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");

        return ResponseEntity.ok(existingUser);
    }

    //session
    @RequestMapping(path = "/api/session", method = RequestMethod.GET)
    public ResponseEntity sessionGet() {
        final UserProfile existingUser = accountService.getUser((Integer) httpSession.getAttribute(PRIMARY_KEY_TO_MAP));
        if (existingUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");

//        System.out.println(existingUser.getLogin()+" "+ existingUser.getPassword()+" "+ existingUser.getEmail());
        return ResponseEntity.ok(existingUser);
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.POST)
    public ResponseEntity sessionLogin(@RequestBody RegistrationRequest body) {
        String login = body.getLogin();
        String password = body.getPassword();

        //validation on front

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
        Integer id = (Integer) httpSession.getAttribute(PRIMARY_KEY_TO_MAP);

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
