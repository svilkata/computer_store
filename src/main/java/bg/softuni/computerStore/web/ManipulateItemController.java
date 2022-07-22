package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.product.AddComputerBindingDTO;
import bg.softuni.computerStore.model.binding.product.ProductItemTypeBindingDTO;
import bg.softuni.computerStore.service.ComputerService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/pages/purchases")
public class ManipulateItemController {
    private final ComputerService computerService;

    public ManipulateItemController(ComputerService computerService) {
        this.computerService = computerService;
    }

    @GetMapping("/items/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE_PURCHASES')")
    public String addNewItemType(Model model) {
        if (!model.containsAttribute("productItemTypeBindingDTO")) {
            model.addAttribute("productItemTypeBindingDTO", new ProductItemTypeBindingDTO());
        }

        return "/purchaseDepartment/addNewItem-choose-type";
    }

    @PostMapping("/items/add")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE_PURCHASES')")
    public String addNewItemTypeConfirm(
            @Valid ProductItemTypeBindingDTO productItemBindingDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productItemTypeBindingDTO", productItemBindingDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productItemTypeBindingDTO",
                    bindingResult);

            return "redirect:/pages/purchases/items/add";
        }

        String type = productItemBindingDTO.getType().toLowerCase(Locale.ROOT);
        return "redirect:/pages/purchases/items/add/" + type;
    }

    @GetMapping("/items/add/computer")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE_PURCHASES')") //тези май не вършат работа
    public String addNewComputer(Model model) {
        if (!model.containsAttribute("addComputerBindingDTO")) {
            model.addAttribute("addComputerBindingDTO", new AddComputerBindingDTO());
        }

        return "/purchaseDepartment/addNewItem-computer";
    }

    @PostMapping("/items/add/computer")
    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE_PURCHASES')") //тези май не вършат работа
    public String addNewComputerConfirm(
            @Valid AddComputerBindingDTO addComputerBindingDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addComputerBindingDTO", addComputerBindingDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addComputerBindingDTO",
                    bindingResult);

            return "redirect:/pages/purchases/items/add/computer";
        }

        Long computerId = this.computerService.saveComputer(addComputerBindingDTO);
        return "redirect:/allitems/computers/" + computerId;
    }
}
