package bg.softuni.computerStore.web;

import bg.softuni.computerStore.exception.BasketIdForbiddenException;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.service.BasketService;
import bg.softuni.computerStore.user.AppUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

@Controller
@RequestMapping("/users/basket")
public class BasketController {
    private final BasketService basketService;

    public BasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    @GetMapping("/{id}")
    public String viewBasketWithItems(Model model,
                                      @PathVariable String id,
                                      @AuthenticationPrincipal AppUser user) {
        final Long userId = isItemIdANumber(id);
        Long basketId = this.basketService.getBaskeIdByUserId(userId);

        if (basketId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketIdForbiddenException(String.format("You do not have authorization for the basket of user with id %d", userId));
        }

        if (!model.containsAttribute("basketId")) {
            model.addAttribute("basketId", basketId);
        }

        return "/customer/OneBasket-items";
    }

    private Long isItemIdANumber(String userId) {
        final Long userLongId;
        try {
            userLongId = Long.parseLong(userId);
        } catch (Exception e){
            throw new ObjectIdNotANumberException(String.format("%s is not a valid userId!", userId));
        }
        return userLongId;
    }

}
