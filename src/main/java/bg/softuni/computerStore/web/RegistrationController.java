package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.user.UserRegisterBindingDTO;
import bg.softuni.computerStore.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class RegistrationController {
    private final UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("userRegistrationModel")
    public void initUserRegistrationModel(Model model){ // in the current controller only!
        model.addAttribute("userRegistrationModel", new UserRegisterBindingDTO());
    }

    @GetMapping("/users/register")
    public String register() {
        // when loading the page for the first time, we automatically will enter into userRegistrationModel == new UserRegisterBindingDto()
        return "/user/auth-registerUser";
    }

    @PostMapping("/users/register")
    public String registerConfirm(@Valid UserRegisterBindingDTO userRegisterBindingDto, BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegistrationModel", userRegisterBindingDto);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userRegistrationModel",
                    bindingResult);
            return "redirect:/users/register";
        }

        userService.registerUserAndAutoLogin(userRegisterBindingDto);
        return "redirect:/";
    }
}
