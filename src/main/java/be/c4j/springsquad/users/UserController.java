package be.c4j.springsquad.users;

import be.c4j.springsquad.users.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.List;

@Controller
public class UserController {

    @RequestMapping("/")
    public ModelAndView showUsers() {

        ModelAndView model = new ModelAndView("users");
        List<User> users = Arrays.asList(
                new User("Davy", 28),
                new User("Jeroen", 26)
        );

        model.addObject("users", users);
        return model;
    }
}
