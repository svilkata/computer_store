package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.view.order.OneBasketViewModel;
import bg.softuni.computerStore.model.view.order.OneItemInBasketViewModel;
import bg.softuni.computerStore.service.BasketService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RestTestController {
    private final BasketService basketService;

    public RestTestController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/basketitems/{basketId}")
//    @PreAuthorize("hasRole('ROLE_BACK_OFFICE') or hasRole('ROLE_FRONT_OFFICE')")
    public OneBasketViewModel getAllModelsForBrandREST(@PathVariable Long basketId) {
        OneBasketViewModel basket = this.basketService.viewAllItemsFromOneBasket(basketId);
        return basket;
    }

}
