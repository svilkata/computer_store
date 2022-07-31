package bg.softuni.computerStore.web;

import bg.softuni.computerStore.exception.BasketIdForbiddenException;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.model.view.order.OneBasketViewModel;
import bg.softuni.computerStore.service.BasketService;
import bg.softuni.computerStore.user.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class RestTestController {
    private final BasketService basketService;

    public RestTestController(BasketService basketService) {
        this.basketService = basketService;
    }

    //basketId
    @GetMapping("/users/basket/viewitems/{id}")
    public OneBasketViewModel getBasketWithAllItems(@PathVariable String id,
                                                    @AuthenticationPrincipal AppUser user) {
        final Long basketId = isItemIdANumber(id);
        Long userId = this.basketService.getUserIdByBasketId(basketId);

        if (userId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketIdForbiddenException(String.format("You do not have authorization to the basket items of user with id %d", userId));
        }

        OneBasketViewModel basket = this.basketService.viewAllItemsFromOneBasket(basketId);
        return basket;
    }

    @GetMapping("/users/basket/additemtobasket/{itemId}")
    public ResponseEntity<String> addItemToBasket(@PathVariable Long itemId,
                                                     @AuthenticationPrincipal AppUser user) {
        Long basketId = this.basketService.getBaskeIdByUserId(user.getId());
        boolean addingResult = this.basketService.addNewItemToBasket(itemId, basketId);

        //if condition - return response will be specific, and then I will be able to call alert message dialog box
        return addingResult
                ? ResponseEntity.noContent().build()  //No content 204 Successfull
                : ResponseEntity.badRequest().build();  //Bad request 400
    }

    private Long isItemIdANumber(String basketId) {
        final Long userLongId;
        try {
            userLongId = Long.parseLong(basketId);
        } catch (Exception e) {
            throw new ObjectIdNotANumberException(String.format("%s is not a valid basketId!", basketId));
        }
        return userLongId;
    }

}
