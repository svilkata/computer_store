package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.user.UserLoginBindingDto;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class LoginController {
    @GetMapping("/users/login")
    public String login(Model model) {
        if (!model.containsAttribute("userLoginDto")) {
            model.addAttribute("userLoginDto", new UserLoginBindingDto());
            model.addAttribute("notFound", false);
        }

        return "/user/auth-login";
    }

    // NOTE: This should be post mapping!
    @PostMapping("/users/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            @Valid UserLoginBindingDto userLoginBindingDto,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        redirectAttributes
                .addFlashAttribute("userLoginDto", userLoginBindingDto)
                .addFlashAttribute("org.springframework.validation.BindingResult.userLoginDto", bindingResult)
                .addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username)
                .addFlashAttribute("bad_credentials", true);

        return "redirect:/users/login";
    }

}
