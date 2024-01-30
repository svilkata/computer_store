package bg.softuni.computerStore.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class LogoutController {
    //for automatic logout after changed admin user or changed password
    @GetMapping("/users/logout")
    public String logOut(HttpSession httpSession) throws Exception {
        httpSession.invalidate();

        return "redirect:/";
    }
}
