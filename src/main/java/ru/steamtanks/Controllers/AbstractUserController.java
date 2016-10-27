package ru.steamtanks.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.steamtanks.main.RegistrationController;
import ru.steamtanks.services.UserService.UserServiceI;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by nikita on 25.10.16.
 */
@RestController
@CrossOrigin()
public abstract class AbstractUserController {


}
