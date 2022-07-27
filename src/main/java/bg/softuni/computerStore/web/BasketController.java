package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.view.order.OneBasketViewModel;
import bg.softuni.computerStore.model.view.order.OneItemInBasketViewModel;
import bg.softuni.computerStore.service.BasketService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/baskets")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/{basketId}")
    public String viewBasketWithItems(Model model, @PathVariable Long basketId) {
        if (!model.containsAttribute("basket")) {
            OneBasketViewModel basket = this.basketService.viewAllItemsFromOneBasket(basketId);
            model.addAttribute("basket", basket);
        }

        return "/customer/OneBasket-items";
    }

}
