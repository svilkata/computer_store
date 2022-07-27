package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.model.view.product.MonitorViewGeneralModel;
import bg.softuni.computerStore.service.ComputerService;
import bg.softuni.computerStore.service.MonitorService;
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
    private final MonitorService monitorService;

    public ViewItemsController(ComputerService computerService, MonitorService monitorService) {
        this.computerService = computerService;
        this.monitorService = monitorService;
    }

    @GetMapping("/computer/details/{id}")
    public String viewOneComputer(Model model, @PathVariable Long id) {
        if (!model.containsAttribute("oneComputer")) {
            ComputerViewGeneralModel oneComputer = this.computerService.findOneComputerById(id);
            model.addAttribute("oneComputer", oneComputer);
        }

        return "/viewItems/one-computer-details";
    }

    @GetMapping("/computer")
    public String viewAllComputers(Model model) {
        if (!model.containsAttribute("computers")) {
            List<ComputerViewGeneralModel> computers = this.computerService.findAllComputers();
            model.addAttribute("computers", computers);
        }

        return "/viewItems/all-computers";
    }


    @GetMapping("/monitor/details/{id}")
    public String viewOneMonitor(Model model, @PathVariable Long id) {
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



}
