package bg.softuni.computer_store.web;

import bg.softuni.computer_store.model.binding.UserLoginDto;
import bg.softuni.computer_store.model.binding.UserRegisterDTO;
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

//  @ModelAttribute("userLoginModel")
//  public void initUserLoginModel(Model model){ //изпълнява се в рамките на текущия контролер само!!!
//    model.addAttribute("userLoginModel", new UserLoginDto());
//    model.addAttribute("notFound", false);
//  }

    @GetMapping("/users/login")
    public String login(Model model) {
        if (!model.containsAttribute("userLoginModel")) {
            model.addAttribute("userLoginModel", new UserLoginDto());
            model.addAttribute("notFound", false);
        }

        return "auth-login";
    }

    @PostMapping("/users/login")
    public String login(@Valid UserLoginDto userLoginDto,
                        BindingResult bindingResult,
                        RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userLoginDto", userLoginDto);
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.userLoginDto", bindingResult);

            return "redirect:/users/login";
        }

//        //User login
//        if (!this.userAuthService.login(userLoginBindingDto)) {
//            redirectAttributes.addFlashAttribute("userLoginBindingDto", userLoginBindingDto);
//            redirectAttributes.addFlashAttribute("notFound", true);
//
//            return "redirect:login";
//        }
        return "redirect:/users/login";
    }

    // NOTE: This should be post mapping!
    @PostMapping("/users/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String userName,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, userName);
        redirectAttributes.addFlashAttribute("notFound",
                true);

        return "redirect:/users/login";
    }

}
