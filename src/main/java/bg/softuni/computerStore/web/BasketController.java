package bg.softuni.computerStore.web;

import bg.softuni.computerStore.exception.BasketIdForbiddenException;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.model.binding.order.ClientOrderExtraInfoGetViewModel;
import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;
import bg.softuni.computerStore.model.view.order.OneBasketViewModel;
import bg.softuni.computerStore.service.BasketService;
import bg.softuni.computerStore.user.AppUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    //userId
    @GetMapping("/users/basket/{id}")
    public String viewBasketWithItems(Model model,
                                      @PathVariable String id,
                                      @AuthenticationPrincipal AppUser user) {
        final Long userId = isItemIdANumber(id, "userId");
        Long basketId = this.basketService.getBaskeIdByUserId(userId);

        if (basketId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketIdForbiddenException(String.format("You do not have authorization for the basket of user with id %d", userId));
        }

        if (!model.containsAttribute("basketId")) {
            model.addAttribute("basketId", basketId);
        }

        return "/customer/OneBasket-items";
    }

    //userId
    @GetMapping("/users/order/{bId}")
    public String viewOrderWithItemsAndAddAddress(Model model,
                                      @PathVariable String bId,
                                      @AuthenticationPrincipal AppUser user) {
        final Long basketId = isItemIdANumber(bId, "basketId");
        Long userId = this.basketService.getUserIdByBasketId(basketId);

        if (userId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketIdForbiddenException(String.format("You do not have authorization for the basket of user with id %d", userId));
        }

        OneBasketViewModel basket = this.basketService.viewAllItemsFromOneBasket(basketId);

        if (!model.containsAttribute("basket")) {
            model.addAttribute("basket", basket);
        }

        if (!model.containsAttribute("clientOrderExtraInfo")) {
            model.addAttribute("clientOrderExtraInfo", new ClientOrderExtraInfoGetViewModel());
        }


        return "/customer/OneOrder-confirm";
    }

//    @PostMapping("/users/order/{bId}")
//    public String viewOrderWithItemsAndAddAddress(Model model){
//
//        return "redirect:/";
//    }



    private Long isItemIdANumber(String objectId, String comment) {
        final Long userLongId;
        try {
            userLongId = Long.parseLong(objectId);
        } catch (Exception e) {
            throw new ObjectIdNotANumberException(String.format("%s is not a valid %s!", objectId, comment));
        }
        return userLongId;
    }

}
