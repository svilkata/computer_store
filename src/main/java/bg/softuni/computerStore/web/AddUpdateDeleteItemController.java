package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.product.AddUpdateComputerBindingDTO;
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
        Long itemModelIdPresent = -1L;
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
        return "redirect:/pages/purchases/items/add/" + type + "/" + productItemTypeBindingDTO.getModel();
    }

    @GetMapping("/items/add/computer/{modelName}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE_PURCHASES')") //тези май не вършат работа
    public String addNewComputer(Model model, @PathVariable String modelName) {

        if (!model.containsAttribute("addComputerBindingDTO")) {
            AddUpdateComputerBindingDTO newComputerToAdd = new AddUpdateComputerBindingDTO();
            newComputerToAdd.setModel(modelName);
            model.addAttribute("addComputerBindingDTO", newComputerToAdd);
        }

        return "/purchaseDepartment/addNewItem-computer";
    }

    @PostMapping("/items/add/computer/**")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE_PURCHASES')") //тези май не вършат работа
    public String addNewComputerConfirm(
            @Valid AddUpdateComputerBindingDTO addUpdateComputerBindingDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addComputerBindingDTO", addUpdateComputerBindingDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addComputerBindingDTO",
                    bindingResult);

            return "redirect:/pages/purchases/items/add/computer/**";
        }

        Long computerId = this.computerService.saveNewComputer(addUpdateComputerBindingDTO);
        return "redirect:/items/all/computers/details/" + computerId;
    }

    //IMPORTANT - once a customer puts an item in his/her basket, it is not possible to delete the item
    @DeleteMapping("/computers/delete/{id}")
    public String deleteComputer(@PathVariable Long id) {
        this.computerService.deleteComputerAndQuantity(id);

        return "redirect:/items/all/computers";
    }

    @GetMapping("/computers/{id}/edit")
    public String updateComputer(@PathVariable Long id, Model model) {
        AddUpdateComputerBindingDTO editComputer = this.computerService.findComputerByIdUpdatingItem(id);

        if (!model.containsAttribute("editComputerBindingDTO")) {
            model.addAttribute("editComputerBindingDTO", editComputer);
        }

        return "/purchaseDepartment/updateItem-computer";
    }

    @PatchMapping("/computers/{id}/edit")
    public String updateComputerConfirm(@PathVariable Long id,
                                        @Valid AddUpdateComputerBindingDTO addUpdateComputerBindingDTO,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes) {
        addUpdateComputerBindingDTO.setItemId(id);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editComputerBindingDTO", addUpdateComputerBindingDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editComputerBindingDTO",
                    bindingResult);

            return "redirect:/pages/purchases/computers/" + id + "/edit";
        }

        int a = 6;
        this.computerService.updateExistingComputer(addUpdateComputerBindingDTO);

        return "redirect:/items/all/computers/details/" + id;
    }
}
