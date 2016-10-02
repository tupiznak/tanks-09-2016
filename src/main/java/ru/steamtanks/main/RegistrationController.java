package ru.steamtanks.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.steamtanks.models.UserProfile;
import ru.steamtanks.services.AccauntService;

import javax.servlet.http.HttpSession;
import java.util.Objects;

@RestController
@CrossOrigin()
public class RegistrationController {

    final private AccauntService accauntService;
    final private HttpSession httpSession;

    @Autowired
    public RegistrationController(AccauntService accauntService,
                                  HttpSession httpSession) {
        this.accauntService = accauntService;
        this.httpSession = httpSession;
    }

    //user
    @RequestMapping(path = "/api/user", method = RequestMethod.POST)
    public ResponseEntity userAdd(@RequestBody RegistrationRequest body) {
        String login = body.getLogin();
        String email = body.getEmail();
        String password = body.getPassword();

        //validation on front

        final UserProfile existingUser = accauntService.getUser(login);
        if (existingUser != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");

        accauntService.addUser(login, password, email);

        httpSession.setAttribute("username", login);

        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.DELETE)
    public ResponseEntity userDel() {
        String login = (String) httpSession.getAttribute("username");

        if (StringUtils.isEmpty(login))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");

        accauntService.delUser(login);
        sessionDel();

        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.PUT)
    public ResponseEntity userCange(@RequestBody RegistrationRequest body) {
        String login = body.getLogin();
        String email = body.getEmail();
        String password = body.getPassword();

        //validation on front

        final UserProfile existingUser = accauntService.getUser(login);
        if (existingUser == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");

        existingUser.setLogin(login);
        existingUser.setEmail(email);
        existingUser.setPassword(password);

        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    //session
    @RequestMapping(path = "/api/session", method = RequestMethod.GET)
    public ResponseEntity sessionGet() {

        final UserProfile existingUser = accauntService.getUser((String) httpSession.getAttribute("username"));
        if (existingUser==null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");

        return ResponseEntity.ok().body(
                "{\"login\":\""+existingUser.getLogin()+"\"," +
                        "\"email\":\""+ existingUser.getEmail()+"\"}"
        );
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.POST)
    public ResponseEntity sessionLogin(@RequestBody RegistrationRequest body) {
        String login = body.getLogin();
        String password = body.getPassword();

        //validation on front

        final UserProfile existingUser = accauntService.getUser(login);
        if (existingUser == null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");

        if (!Objects.equals(existingUser.getPassword(), password))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");

        httpSession.setAttribute("username", login);

        return ResponseEntity.status(HttpStatus.OK).body(
                "{\"email\":\""+ existingUser.getEmail()+"\"}"
        );
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.DELETE)
    public ResponseEntity sessionDel() {
        String login = (String) httpSession.getAttribute("username");

        if (StringUtils.isEmpty(login))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");

        httpSession.setAttribute("username", null);

        return ResponseEntity.status(HttpStatus.OK).body("{}");
    }

    private static final class RegistrationRequest {
        private String login;
        private String email;
        private String password;

        public String getLogin() {
            return login;
        }

        public String getEmail() {
            return email;
        }

        public String getPassword() {
            return password;
        }
    }
}
