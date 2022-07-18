package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.ChangeUserPasswordDTO;
import bg.softuni.computerStore.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ChangePasswordController {
    private final UserService userService;
    private final PasswordEncoder encoder;

    public ChangePasswordController(UserService userService, PasswordEncoder encoder) {
        this.userService = userService;
        this.encoder = encoder;
    }

    @GetMapping("/users/changepassword")
    public String changePassword(Model model) {
        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        model.addAttribute("currentUsername", username);
        if (!model.containsAttribute("changeUserPasswordDTO")) {
            model.addAttribute("changeUserPasswordDTO", new ChangeUserPasswordDTO());
        }
        return "/user/auth-changePassword";
    }

    @PostMapping("/users/changepassword")
    public String register(@Valid ChangeUserPasswordDTO changeUserPasswordDTO,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        changeUserPasswordDTO.setUsername(username);

        String passwordCurrentUserEncoded = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getPassword();
        String passwordUserEnteredRaw = changeUserPasswordDTO.getCurrentPassword();

        boolean areEquals = this.encoder.matches(passwordUserEnteredRaw, passwordCurrentUserEncoded);

        if (!areEquals) {
            redirectAttributes.addFlashAttribute("changeUserPasswordDTO", changeUserPasswordDTO);
            redirectAttributes.addFlashAttribute("userEnteredWrongCurrentPass", true);

            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changeUserPasswordDTO",
                        bindingResult);
            }

            return "redirect:/users/changepassword";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("changeUserPasswordDTO", changeUserPasswordDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.changeUserPasswordDTO",
                    bindingResult);

            return "redirect:/users/changepassword";
        }

        userService.changeCurrentUserPassword(changeUserPasswordDTO);
        return "redirect:/";
    }


}
