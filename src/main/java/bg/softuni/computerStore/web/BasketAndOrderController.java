package bg.softuni.computerStore.web;

import bg.softuni.computerStore.exception.BasketIdForbiddenException;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.model.binding.order.ClientOrderExtraInfoGetViewModel;
import bg.softuni.computerStore.model.view.order.OneBasketViewModel;
import bg.softuni.computerStore.service.BasketService;
import bg.softuni.computerStore.service.FinalOrderService;
import bg.softuni.computerStore.user.AppUser;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Objects;

@Controller
public class BasketAndOrderController {
    private final BasketService basketService;
    private final FinalOrderService finalOrderService;

    public BasketAndOrderController(BasketService basketService, FinalOrderService finalOrderService) {
        this.basketService = basketService;
        this.finalOrderService = finalOrderService;
    }

    //userId == uId
    @GetMapping("/users/basket/{uId}")
    public String viewBasketWithItems(Model model,
                                      @PathVariable String uId,
                                      @AuthenticationPrincipal AppUser user) {
        final Long userId = isItemIdANumber(uId, "userId");
        Long basketId = this.basketService.getBaskeIdByUserId(userId);

        if (basketId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketIdForbiddenException(String.format("You do not have authorization for the basket of user with id %d", userId));
        }

        if (!model.containsAttribute("basketId")) {
            model.addAttribute("basketId", basketId);
        }

        return "/customer/OneBasket-items";
    }

    //basketId == bId
    @GetMapping("/users/order/{bId}")
    public String viewOrderWithItemsAndAddAddress(Model model,
                                                  @PathVariable String bId,
                                                  @AuthenticationPrincipal AppUser user) {
        final Long basketId = isItemIdANumber(bId, "basketId");
        Long userId = this.basketService.getUserIdByBasketId(basketId);

        if (userId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketIdForbiddenException(String.format("You do not have authorization for the basket/order of user with id %d", userId));
        }

        OneBasketViewModel basket = this.basketService.viewAllItemsFromOneBasket(basketId);

        if (!model.containsAttribute("basket")) {
            model.addAttribute("basket", basket);
        }

        if (!model.containsAttribute("clientOrderExtraInfo")) {
            model.addAttribute("clientOrderExtraInfo", new ClientOrderExtraInfoGetViewModel());
        }

        if (!model.containsAttribute("userId")) {
            model.addAttribute("userId", userId);
        }

        if (!model.containsAttribute("basketId")) {
            model.addAttribute("basketId", basketId);
        }

        return "/customer/OneOrder-confirm";
    }

    //basketId == bId
    @PostMapping("/users/order/{bId}")
    public String viewOrderWithItemsAndAddAddressConfirm(@Valid ClientOrderExtraInfoGetViewModel clientExtraOrderInfo,
                                                         BindingResult bindingResult,
                                                         RedirectAttributes redirectAttributes,
                                                         @PathVariable String bId,
                                                         @AuthenticationPrincipal AppUser user) {
        final Long basketId = isItemIdANumber(bId, "basketId");
        Long userId = this.basketService.getUserIdByBasketId(basketId);

        if (userId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketIdForbiddenException(String.format("You do not have authorization for the basket/order of user with id %d", userId));
        }

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("clientExtraOrderInfo", clientExtraOrderInfo);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.clientExtraOrderInfo",
                    bindingResult);

            OneBasketViewModel basket = this.basketService.viewAllItemsFromOneBasket(basketId);
            redirectAttributes.addFlashAttribute("basket", basket);

            redirectAttributes.addFlashAttribute("userId", user.getId());
            redirectAttributes.addFlashAttribute("basketId", basketId);

            return "redirect:/users/order/" + basketId;
        }

        //Creation order is successfull, we start creating the order
        this.finalOrderService.processOrder(basketId, clientExtraOrderInfo);

        return "redirect:/";
    }

//    //Една страница където ще се зареждат поръчките на даден user или всички поръчки за SALES и ADMIN роли
//    @GetMapping("/users/vieworders/")


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
