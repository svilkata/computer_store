package bg.softuni.computerStore.web;

import bg.softuni.computerStore.exception.BasketForbiddenException;
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

    //bId is basketId
    @GetMapping("/users/basket/viewitems/{bId}")
    public ResponseEntity<OneBasketViewModel> getBasketWithAllItems(@PathVariable String bId,
                                                                    @AuthenticationPrincipal AppUser user) {
        final Long basketId = isItemIdANumber(bId, "basketId");
        Long userId = this.basketService.getUserIdByBasketId(basketId);

        if (userId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketForbiddenException(String.format("You do not have authorization to the basket items of user with id %d", userId));
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
            throw new BasketForbiddenException(String.format("You do not have authorization to the basket items of user with id %d", userId));
        }

        final Long itemId = isItemIdANumber(iId, "itemId");
        final Long newQuantity = isItemIdANumber(newQ, "newQuantity");

        int changeQuantityResult = this.basketService.changeOrderedQuantity(basketId, itemId, newQuantity);
        OneBasketViewModel basket = this.basketService.viewAllItemsFromOneBasket(basketId);

        //if condition - return response will be specific, and then I will be able to call alert message dialog box
        return switch (changeQuantityResult) {
            case 1 -> ResponseEntity.ok(basket);  //Successfull 200 //alert('Quantity changed successfully');
            case -1 -> ResponseEntity.accepted().body(basket);  //Accepted 202  //alert('Last quantities of the changed item left!');
            case -2 -> ResponseEntity.status(HttpStatus.CREATED).body(basket);  //Created 201  //not used yet in the project in reality this response, but I use alert('Quantity can not be negative or zero'); on the JS
            default -> throw new IllegalStateException("Unexpected value: " + changeQuantityResult);
        };
    }

    //bId is basketId
    @GetMapping("/users/basket/removeOneItemFromBasket/{bId}")
    public ResponseEntity<OneBasketViewModel> removeOneItemFromBasket(@PathVariable String bId,
                                                                      @RequestParam("itemId") String iId,
                                                                      @AuthenticationPrincipal AppUser user) {
        final Long basketId = isItemIdANumber(bId, "basketId");
        Long userId = this.basketService.getUserIdByBasketId(basketId);

        if (userId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketForbiddenException(String.format("You do not have authorization to the basket items of user with id %d", userId));
        }

        final Long itemId = isItemIdANumber(iId, "itemId");

        this.basketService.removeOneItemFromBasket(basketId, itemId);
        OneBasketViewModel basket = this.basketService.viewAllItemsFromOneBasket(basketId);

        //if condition - return response will be specific, and then I will be able to call alert message dialog box

        return ResponseEntity.ok(basket);  //Successfull 200
    }

    @GetMapping("/users/basket/additemtobasket/{itmId}")
    public ResponseEntity<String> addItemToBasket(@PathVariable String itmId,
                                                  @AuthenticationPrincipal AppUser user) {
        final Long itemId = isItemIdANumber(itmId, "ItemId");

        //here no need to check if the user has access - we take the basketId from the userId

        Long basketId = this.basketService.getBaskeIdByUserId(user.getId());
        int addingResult = this.basketService.addNewItemToBasket(itemId, basketId);

        //if condition - return response will be specific, and then I will be able to call alert message dialog box
        return switch (addingResult) {
            case 1 -> ResponseEntity.accepted().build();  //No content 202 Successfull  //alert('You have successfully added the item in your basket!')
            case -1 -> ResponseEntity.badRequest().build();  //Bad request 400   //alert('This item is already added to your basket! You can not add a second time this item in your basket!')
            case -2 -> ResponseEntity.noContent().build();  //No content 204 Successfull //alert('This item has ZERO quantity at the moment! You can not add it in your basket right now!')
            default -> throw new IllegalStateException("Unexpected value: " + addingResult);
        };
    }

    private Long isItemIdANumber(String commentId, String comment) {
        final Long userLongId;
        try {
            userLongId = Long.parseLong(commentId);
        } catch (Exception e) {
            throw new ObjectIdNotANumberException(String.format("%s is not a valid %s! number", commentId, comment));
        }
        return userLongId;
    }

}
