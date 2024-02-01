package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.product.ProductItemTypeBindingDTO;
import bg.softuni.computerStore.service.AllItemsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/pages/purchases")
public class AddNewItemController {
    private final AllItemsService allItemsService;

    public AddNewItemController(AllItemsService allItemsService) {
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
    public String addNewItemTypeConfirm(@Valid ProductItemTypeBindingDTO productItemTypeBindingDTO,
            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
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
}
