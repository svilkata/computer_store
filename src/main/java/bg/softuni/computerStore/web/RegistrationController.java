package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.UserRegisterBindingDTO;
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
    public void initUserRegistrationModel(Model model){ //изпълнява се в рамките на текущия контролер само!!!
        model.addAttribute("userRegistrationModel", new UserRegisterBindingDTO());
    }

    @GetMapping("/users/register")
    public String register() {
        // когато зареждаме за първи път страницата, то автомиатично ще влезе към модела атрибут userModel == празен new UserRegisterBindingDto()
        return "/user/auth-registerUser";
    }

    @PostMapping("/users/register")
    public String register(@Valid UserRegisterBindingDTO userRegisterBindingDto,
                           BindingResult bindingResult,
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
