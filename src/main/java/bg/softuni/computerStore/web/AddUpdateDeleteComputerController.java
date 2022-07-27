package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.product.AddUpdateComputerBindingDTO;
import bg.softuni.computerStore.service.ComputerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/pages/purchases")
public class AddUpdateDeleteComputerController {
    private final ComputerService computerService;

    public AddUpdateDeleteComputerController(ComputerService computerService) {
        this.computerService = computerService;
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
        return "redirect:/items/all/computer/details/" + computerId;
    }

    //IMPORTANT - once a customer puts an item in his/her basket, it is not possible to delete the item
    //We better delete in the beginnig before ordering - in case we have done a mistake
    @DeleteMapping("/computers/delete/{id}")
    public String deleteComputer(@PathVariable Long id) {
        this.computerService.deleteComputerAndQuantity(id);

        return "redirect:/items/all/computer";
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

        this.computerService.updateExistingComputer(addUpdateComputerBindingDTO);

        return "redirect:/items/all/computer/details/" + id;
    }
}
