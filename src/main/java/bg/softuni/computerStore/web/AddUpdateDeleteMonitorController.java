package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.product.AddUpdateMonitorBindingDTO;
import bg.softuni.computerStore.service.ComputerService;
import bg.softuni.computerStore.service.MonitorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/pages/purchases")
public class AddUpdateDeleteMonitorController {
    private final MonitorService monitorService;

    public AddUpdateDeleteMonitorController(MonitorService monitorService) {
        this.monitorService = monitorService;
    }

    @GetMapping("/items/add/monitor/{modelName}")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE_PURCHASES')") //тези май не вършат работа
    public String addNewMonitor(Model model, @PathVariable String modelName) {

        if (!model.containsAttribute("addMonitorBindingDTO")) {
            AddUpdateMonitorBindingDTO newMonitorToAdd = new AddUpdateMonitorBindingDTO();
            newMonitorToAdd.setModel(modelName);
            model.addAttribute("addMonitorBindingDTO", newMonitorToAdd);
        }

        return "/purchaseDepartment/addNewItem-monitor";
    }

    @PostMapping("/items/add/monitor/**")
//    @PreAuthorize("hasRole('ROLE_ADMIN') || hasRole('ROLE_EMPLOYEE_PURCHASES')") //тези май не вършат работа
    public String addNewMonitorConfirm(
            @Valid AddUpdateMonitorBindingDTO addUpdateMonitorBindingDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addMonitorBindingDTO", addUpdateMonitorBindingDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.addMonitorBindingDTO",
                    bindingResult);

            return "redirect:/pages/purchases/items/add/monitor/**";
        }

        Long monitorId = this.monitorService.saveNewMonitor(addUpdateMonitorBindingDTO);
        return "redirect:/items/all/monitors/details/" + monitorId;
    }

    //IMPORTANT - once a customer puts an item in his/her basket, it is not possible to delete the item
    //We better delete in the beginning before ordering - in case we have done a mistake
    @DeleteMapping("/monitors/delete/{id}")
    public String deleteMonitor(@PathVariable Long id) {
        this.monitorService.deleteMonitorAndQuantity(id);

        return "redirect:/items/all/monitors";
    }

    @GetMapping("/monitors/{id}/edit")
    public String updateMonitor(@PathVariable Long id, Model model) {
        AddUpdateMonitorBindingDTO editMonitor = this.monitorService.findMonitorByIdUpdatingItem(id);

        if (!model.containsAttribute("editMonitorBindingDTO")) {
            model.addAttribute("editMonitorBindingDTO", editMonitor);
        }

        return "/purchaseDepartment/updateItem-monitor";
    }

    @PatchMapping("/monitors/{id}/edit")
    public String updateMonitorConfirm(@PathVariable Long id,
                                        @Valid AddUpdateMonitorBindingDTO addUpdateMonitorBindingDTO,
                                        BindingResult bindingResult,
                                        RedirectAttributes redirectAttributes) {
        addUpdateMonitorBindingDTO.setItemId(id);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("editMonitorBindingDTO", addUpdateMonitorBindingDTO);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.editMonitorBindingDTO",
                    bindingResult);

            return "redirect:/pages/purchases/monitors/" + id + "/edit";
        }

        this.monitorService.updateExistingMonitor(addUpdateMonitorBindingDTO);

        return "redirect:/items/all/monitors/details/" + id;
    }
}
