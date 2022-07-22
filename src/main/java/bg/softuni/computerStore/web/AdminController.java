package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.user.EmployeeRegisterBindingDTO;
import bg.softuni.computerStore.model.binding.user.UserRolesBindingDTO;
import bg.softuni.computerStore.service.StatsService;
import bg.softuni.computerStore.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/pages/admins")
public class AdminController {
    private final UserService userService;
    private final StatsService statsService;

    public AdminController(UserService userService, StatsService statsService) {
        this.userService = userService;
        this.statsService = statsService;
    }

    @GetMapping("/statistics")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public ModelAndView statistics() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("stats", statsService.getStats());
        modelAndView.setViewName("/user/stats");
        return modelAndView;
    }

    @GetMapping("/set-user-role")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String addEmployeeRole(Model model) {
        model.addAttribute("employees", this.userService.getEmployeeUsers());
        if (!model.containsAttribute("userRolesBindingDTO")) {
            model.addAttribute("userRolesBindingDTO", new UserRolesBindingDTO());
        }
        return "/user/add-or-edit-user-role";
    }

    @PostMapping("/set-user-role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String addUserRoleConfirm(@Valid UserRolesBindingDTO userRolesBindingDTO, RedirectAttributes redirectAttributes) {
        if (userRolesBindingDTO.getUsername().isEmpty()) {
            redirectAttributes.addFlashAttribute("userRolesBindingDTO", userRolesBindingDTO);
            redirectAttributes.addFlashAttribute("employeeNotSelected", true);
            return "redirect:/pages/admins/set-user-role";
        }

        if (userRolesBindingDTO.getRoles().size() < 2) {
            redirectAttributes.addFlashAttribute("userRolesBindingDTO", userRolesBindingDTO);
            redirectAttributes.addFlashAttribute("atLeastTwoRolesShouldBeSelected", true);
            return "redirect:/pages/admins/set-user-role";
        }

        if (userRolesBindingDTO.getRoles().contains("ADMIN")) {
            redirectAttributes.addFlashAttribute("userRolesBindingDTO", userRolesBindingDTO);
            redirectAttributes.addFlashAttribute("oneAdminOnlyPossible", true);
            return "redirect:/pages/admins/set-user-role";
        }

        this.userService.setEmployeeRoles(userRolesBindingDTO);
        return "index";
    }

    @GetMapping("/change-admin-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeAdminUser(Model model) {
        model.addAttribute("employees", this.userService.getEmployeeUsers());

        if (!model.containsAttribute("userRolesBindingDTO")) {
            model.addAttribute("userRolesBindingDTO", new UserRolesBindingDTO());
        }
        return "/user/change-admin-user-role";
    }

    @PostMapping("/change-admin-user")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String changeAdminUserConfirm(@Valid UserRolesBindingDTO userRolesBindingDTO,
                                         RedirectAttributes redirectAttributes) {
        if (userRolesBindingDTO.getUsername().isEmpty()) {
            redirectAttributes.addFlashAttribute("userRolesBindingDTO", userRolesBindingDTO);
            redirectAttributes.addFlashAttribute("employeeNotSelected", true);
            return "redirect:/pages/admins/change-admin-user";
        }

        //all 4 checkboxes selected
        if (userRolesBindingDTO.getRoles().size() < 4) {
            redirectAttributes.addFlashAttribute("userRolesBindingDTO", userRolesBindingDTO);
            redirectAttributes.addFlashAttribute("adminChanging", true);
            return "redirect:/pages/admins/change-admin-user";
        }

        //we first disable the Admin role of the current Admin User
        this.userService.disableAdminRoleForCurrentAdminUser();

        //Then, we set the Admin role for the desired employee
        this.userService.setEmployeeRoles(userRolesBindingDTO);

        //Finally, we log out automatically
        return "redirect:/users/logout";
    }

    @GetMapping("/register-new-employee")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String registerNewEmployee(Model model) {
        if (!model.containsAttribute("employeeRegistrationModel")) {
            model.addAttribute("employeeRegistrationModel", new EmployeeRegisterBindingDTO());
        }
        return "/user/registerNewEmployee";
    }

    @PostMapping("/register-new-employee")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String registerNewEmployeeConfirm(@Valid EmployeeRegisterBindingDTO employeeRegistrationModel,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {

        if (employeeRegistrationModel.getRoles().size() < 2) {
            redirectAttributes.addFlashAttribute("atLeastTwoRolesShouldBeSelected", true);
            redirectAttributes.addFlashAttribute("employeeRegistrationModel", employeeRegistrationModel);
            if (bindingResult.hasErrors()) {
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employeeRegistrationModel",
                        bindingResult);
            }
            return "redirect:/pages/admins/register-new-employee";
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("employeeRegistrationModel", employeeRegistrationModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.employeeRegistrationModel",
                    bindingResult);
            return "redirect:/pages/admins/register-new-employee";
        }

        userService.registerEmployee(employeeRegistrationModel);
        return "redirect:/";
    }

}
