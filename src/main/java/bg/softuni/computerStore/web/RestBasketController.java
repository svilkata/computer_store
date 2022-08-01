package bg.softuni.computerStore.web;

import bg.softuni.computerStore.exception.BasketIdForbiddenException;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.model.view.order.OneBasketViewModel;
import bg.softuni.computerStore.service.BasketService;
import bg.softuni.computerStore.user.AppUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class RestBasketController {
    private final BasketService basketService;

    public RestBasketController(BasketService basketService) {
        this.basketService = basketService;
    }

    //id is basketId
    @GetMapping("/users/basket/viewitems/{id}")
    public ResponseEntity<OneBasketViewModel> getBasketWithAllItems(@PathVariable String id,
                                                                    @AuthenticationPrincipal AppUser user) {
        final Long basketId = isItemIdANumber(id, "basketId");
        Long userId = this.basketService.getUserIdByBasketId(basketId);

        if (userId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketIdForbiddenException(String.format("You do not have authorization to the basket items of user with id %d", userId));
        }

        OneBasketViewModel basket = this.basketService.viewAllItemsFromOneBasket(basketId);
        return ResponseEntity.ok(basket);
    }

    //id is basketId
    @GetMapping("/users/basket/changeOneItemQuantityInBasket/{bId}")
    public ResponseEntity<OneBasketViewModel> changeOneItemQuantityInBasket(@PathVariable String bId,
                                                                            @RequestParam("itemId") String iId,
                                                                            @RequestParam("newQuantity") String newQ,
                                                                            @AuthenticationPrincipal AppUser user) {

        final Long basketId = isItemIdANumber(bId, "basketId");
        Long userId = this.basketService.getUserIdByBasketId(basketId);

        if (userId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketIdForbiddenException(String.format("You do not have authorization to the basket items of user with id %d", userId));
        }

        final Long itemId = isItemIdANumber(iId, "itemId");
        final Long newQuantity = isItemIdANumber(newQ, "newQuantity");

        int changeQuantityResult = this.basketService.changeOrderedQuantity(basketId, itemId, newQuantity);
        OneBasketViewModel basket = this.basketService.viewAllItemsFromOneBasket(basketId);

        //if condition - return response will be specific, and then I will be able to call alert message dialog box
        return switch (changeQuantityResult) {
            case 1 -> ResponseEntity.ok(basket);  //Successfull 200
            case -1 -> ResponseEntity.accepted().body(basket);  //Accepted 202
            case -2 -> ResponseEntity.status(HttpStatus.CREATED).body(basket);  //Created 201
            default -> throw new IllegalStateException("Unexpected value: " + changeQuantityResult);
        };
    }

    //id is basketId
    @GetMapping("/users/basket/removeOneItemFromBasket/{bId}")
    public ResponseEntity<OneBasketViewModel> removeOneItemFromBasket(@PathVariable String bId,
                                                                      @RequestParam("itemId") String iId,
                                                                      @AuthenticationPrincipal AppUser user) {
        final Long basketId = isItemIdANumber(bId, "basketId");
        Long userId = this.basketService.getUserIdByBasketId(basketId);

        if (userId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketIdForbiddenException(String.format("You do not have authorization to the basket items of user with id %d", userId));
        }

        final Long itemId = isItemIdANumber(iId, "itemId");

        this.basketService.removeOneItemFromBasket(basketId, itemId);
        OneBasketViewModel basket = this.basketService.viewAllItemsFromOneBasket(basketId);

        //if condition - return response will be specific, and then I will be able to call alert message dialog box

        return ResponseEntity.ok(basket);  //Successfull 200
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

    private Long isItemIdANumber(String basketId, String comment) {
        final Long userLongId;
        try {
            userLongId = Long.parseLong(basketId);
        } catch (Exception e) {
            throw new ObjectIdNotANumberException(String.format("%s is not a valid %s! number", basketId, comment));
        }
        return userLongId;
    }

}
