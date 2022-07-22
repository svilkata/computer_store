package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.product.AddComputerBindingDTO;
import bg.softuni.computerStore.model.binding.product.ProductItemTypeBindingDTO;
import bg.softuni.computerStore.service.AllItemsService;
import bg.softuni.computerStore.service.ComputerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/pages/purchases")
public class AddUpdateDeleteItemController {
    private final ComputerService computerService;
    private final AllItemsService allItemsService;

    public AddUpdateDeleteItemController(ComputerService computerService, AllItemsService allItemsService) {
        this.computerService = computerService;
        this.allItemsService = allItemsService;
    }

    @GetMapping("/items/add")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE_PURCHASES')")
    public String addNewItemType(Model model) {
        if (!model.containsAttribute("productItemTypeBindingDTO")) {
            model.addAttribute("productItemTypeBindingDTO", new ProductItemTypeBindingDTO());
        }

        if (!model.containsAttribute("modelExists")) {
            model.addAttribute("modelExists", false);
        }

        if (!model.containsAttribute("href")) {
            model.addAttribute("href", null);
        }

        return "/purchaseDepartment/addNewItem-choose-type-and-model";
    }

    @PostMapping("/items/add")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE_PURCHASES')")
    public String addNewItemTypeConfirm(
            @Valid ProductItemTypeBindingDTO productItemTypeBindingDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        Long itemModelIdPresent = -2L;
        if (!productItemTypeBindingDTO.getModel().isBlank()) {
            itemModelIdPresent = this.allItemsService.isItemModelPresent(productItemTypeBindingDTO.getModel());
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("productItemTypeBindingDTO", productItemTypeBindingDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productItemTypeBindingDTO",
                    bindingResult);

            return "redirect:/pages/purchases/items/add";
        }

        String type = productItemTypeBindingDTO.getType().toLowerCase(Locale.ROOT);
        if (itemModelIdPresent > 0) {
            redirectAttributes.addFlashAttribute("productItemTypeBindingDTO", productItemTypeBindingDTO);
            redirectAttributes.addFlashAttribute("modelExists", true);

            //we prepare here for going directly to the update/edit page
            StringBuilder hrefPrep = new StringBuilder();
            hrefPrep.append("/pages/purchases/")
                    .append(type).append("s")
                    .append("/").append("{id}/edit");
//            th:href="@{/pages/purchases/computers/{id}/edit (id=*{itemId})}"
            String href = hrefPrep.toString();
            productItemTypeBindingDTO.setItemId(itemModelIdPresent);
            redirectAttributes.addFlashAttribute("href", href);


            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.productItemTypeBindingDTO",
                    bindingResult);

            return "redirect:/pages/purchases/items/add";
        }

        //we go here for adding the relevant new item
        return "redirect:/pages/purchases/items/add/" + type;
    }

    @GetMapping("/items/add/computer")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE_PURCHASES')") //тези май не вършат работа
    public String addNewComputer(Model model) {
        if (!model.containsAttribute("addComputerBindingDTO")) {
            model.addAttribute("addComputerBindingDTO", new AddComputerBindingDTO());
        }

        return "/purchaseDepartment/addNewItem-computer";
    }

    @PostMapping("/items/add/computer")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE_PURCHASES')") //тези май не вършат работа
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

        //TODO да редиректнем със съобщение, че този модел компютър вече съществува


        Long computerId = this.computerService.saveNewComputer(addComputerBindingDTO);
        return "redirect:/items/all/computers" + computerId;
    }

    //IMPORTANT - once a customer puts an item in his/her basket, it is not possible to delete the item
    @DeleteMapping("/computers/delete/{id}")
    public String deleteComputer(@PathVariable Long id) {
        this.computerService.deleteComputerAndQuantity(id);

        return "redirect:/items/all/computers";
    }
}
