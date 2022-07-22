package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.service.ComputerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/items/all")
public class ViewItemsController {
    private final ComputerService computerService;

    public ViewItemsController(ComputerService computerService) {
        this.computerService = computerService;
    }

    @GetMapping("/computers")
    public String viewAllComputers(Model model) {
        if (!model.containsAttribute("computers")) {
            List<ComputerViewGeneralModel> computers = this.computerService.findAllComputers();
            model.addAttribute("computers", computers);
        }

        return "/viewItems/all-computers";
    }

    @GetMapping("/computers/details/{id}")
    public String viewOneComputer(Model model, @PathVariable Long id) {
        if (!model.containsAttribute("oneComputer")) {
            ComputerViewGeneralModel oneComputer = this.computerService.findOneComputerById(id);
            model.addAttribute("oneComputer", oneComputer);
        }

        return "/viewItems/one-computer-details";
    }
}
