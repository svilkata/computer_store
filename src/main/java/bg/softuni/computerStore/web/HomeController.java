package bg.softuni.computerStore.web;

import bg.softuni.computerStore.service.eventServices.GlobalVariablesEventServices;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
    @GetMapping("/")
    public String index(HttpSession httpSession) {
        if (httpSession.getAttribute("totalOrdersCount") == null) {
            httpSession.setAttribute("totalOrdersCount", GlobalVariablesEventServices.totalNumberOfOrders);
        }

        return "index";
    }

    //За всяка заявка или само за заявка потвърждаване на поръчка да сменяме http cookie-то стойността


}
