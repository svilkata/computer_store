package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.product.SearchProductItemDTO;
import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.model.view.product.LaptopViewGeneralModel;
import bg.softuni.computerStore.model.view.product.MonitorViewGeneralModel;
import bg.softuni.computerStore.service.ComputerService;
import bg.softuni.computerStore.service.LaptopService;
import bg.softuni.computerStore.service.MonitorService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/items/all")
public class ViewItemsController {
    private final ComputerService computerService;
    private final MonitorService monitorService;
    private final LaptopService laptopService;

    public ViewItemsController(ComputerService computerService, MonitorService monitorService, LaptopService laptopService) {
        this.computerService = computerService;
        this.monitorService = monitorService;
        this.laptopService = laptopService;
    }

    @GetMapping("/computer/details/{itemId}")
    public String viewOneComputer(Model model, @PathVariable String itemId) {
        if (!model.containsAttribute("oneComputer")) {
            ComputerViewGeneralModel oneComputer = this.computerService.findOneComputerById(itemId);
            model.addAttribute("oneComputer", oneComputer);
        }

        return "/viewItems/one-computer-details";
    }

    @GetMapping("/computer")
    public String viewAllComputers(Model model, @Valid SearchProductItemDTO searchProductItemDTO,
                                   @PageableDefault(page = 0, size = 3, sort = "sellingPrice",
                                           direction = Sort.Direction.ASC) Pageable pageable,
                                   RedirectAttributes redirectAttributes) {

        if (!model.containsAttribute("searchProductItemDTO")) {
            model.addAttribute("searchProductItemDTO", searchProductItemDTO);
        }

//      List<ComputerViewGeneralModel> computers = this.computerService.findAllComputers();
        Page<ComputerViewGeneralModel> computers = this.computerService
                .getAllComputersPageableAndSearched(pageable, searchProductItemDTO, "computer");
        model.addAttribute("computers", computers);

        redirectAttributes.addFlashAttribute("searchProductItemDTO", searchProductItemDTO);

        return "/viewItems/all-computers";
    }


    @GetMapping("/monitor/details/{id}")
    public String viewOneMonitor(Model model, @PathVariable String id) {
        if (!model.containsAttribute("oneMonitor")) {
            MonitorViewGeneralModel oneMonitor = this.monitorService.findOneMonitorById(id);
            model.addAttribute("oneMonitor", oneMonitor);
        }

        return "/viewItems/one-monitor-details";
    }

    @GetMapping("/monitor")
    public String viewAllMonitors(Model model) {
        if (!model.containsAttribute("monitors")) {
            List<MonitorViewGeneralModel> monitors = this.monitorService.findAllMonitors();
            model.addAttribute("monitors", monitors);
        }

        return "/viewItems/all-monitors";
    }

    @GetMapping("/laptop/details/{id}")
    public String viewOneLaptop(Model model, @PathVariable String id) {
        if (!model.containsAttribute("oneLaptop")) {
            LaptopViewGeneralModel oneLaptop = this.laptopService.findOneLaptopById(id);
            model.addAttribute("oneLaptop", oneLaptop);
        }

        return "/viewItems/one-laptop-details";
    }

    @GetMapping("/laptop")
    public String viewAllLaptops(Model model) {
        if (!model.containsAttribute("laptops")) {
            List<LaptopViewGeneralModel> laptops = this.laptopService.findAllLaptops();
            model.addAttribute("laptops", laptops);
        }

        return "/viewItems/all-laptops";
    }


}
