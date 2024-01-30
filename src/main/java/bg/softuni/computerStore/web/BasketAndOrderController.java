package bg.softuni.computerStore.web;

import bg.softuni.computerStore.aop.TrackLatency;
import bg.softuni.computerStore.service.UserService;
import bg.softuni.computerStore.service.eventServices.GlobalVariablesEventServices;
import bg.softuni.computerStore.exception.BasketForbiddenException;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.exception.OrderForbiddenException;
import bg.softuni.computerStore.model.binding.order.ClientOrderExtraInfoGetViewModel;
import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import bg.softuni.computerStore.model.entity.orders.ItemQuantityInOrderEntity;
import bg.softuni.computerStore.model.view.order.OneBasketViewModel;
import bg.softuni.computerStore.model.view.order.OneItemInOrderViewModel;
import bg.softuni.computerStore.model.view.order.OneOrderDetailsViewModel;
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

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class BasketAndOrderController {
    private final BasketService basketService;
    private final FinalOrderService finalOrderService;
    private final UserService userService;

    public BasketAndOrderController(BasketService basketService, FinalOrderService finalOrderService, UserService userService) {
        this.basketService = basketService;
        this.finalOrderService = finalOrderService;
        this.userService = userService;
    }

    //userId == uId
    @GetMapping("/users/basket/{uId}")
    public String viewBasketWithItems(Model model, @PathVariable String uId, @AuthenticationPrincipal AppUser user) {
        final Long userId = isObjectIdANumber(uId, "userId");

        if (userId <= 0 || userId > userService.getCountOfRegisteredUsers()) {
            throw new ObjectIdNotANumberException(String.format("%s is not a valid existing %s number!", userId, "userId"));
        }
        Long basketId = this.basketService.getBaskeIdByUserId(userId);

        if (basketId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketForbiddenException(String.format("You do not have authorization for the basket of user with id %d", userId));
        }

        if (!model.containsAttribute("basketId")) {
            model.addAttribute("basketId", basketId);
        }

        return "/customer/OneBasket-items";
    }

    //basketId == bId
    @GetMapping("/users/order/{bId}")
    public String viewOrderWithItemsAndAddAddress(Model model, @PathVariable String bId, @AuthenticationPrincipal AppUser user) {
        final Long basketId = isObjectIdANumber(bId, "basketId");
        Long userId = this.basketService.getUserIdByBasketId(basketId);

        isBasketIdAValidExistingNumber(basketId);
        doesUserHasAccessToBasket(user, userId);

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
    @TrackLatency(latency = "create order")
    @PostMapping("/users/order/{bId}")
    public String viewOrderWithItemsAndAddAddressConfirm(@Valid ClientOrderExtraInfoGetViewModel clientExtraOrderInfo,
                                                         BindingResult bindingResult,
                                                         RedirectAttributes redirectAttributes,
                                                         @PathVariable String bId,
                                                         @AuthenticationPrincipal AppUser user,
                                                         HttpSession httpSession) {
        final Long basketId = isObjectIdANumber(bId, "basketId");
        Long userId = this.basketService.getUserIdByBasketId(basketId);

        isBasketIdAValidExistingNumber(basketId);
        doesUserHasAccessToBasket(user, userId);

        OneBasketViewModel basket = this.basketService.viewAllItemsFromOneBasket(basketId);
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("clientExtraOrderInfo", clientExtraOrderInfo);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.clientExtraOrderInfo",
                    bindingResult);

            redirectAttributes.addFlashAttribute("basket", basket);

            redirectAttributes.addFlashAttribute("userId", user.getId());
            redirectAttributes.addFlashAttribute("basketId", basketId);

            return "redirect:/users/order/" + basketId;
        }

        //Creation order is successful, we start creating the order
        String orderNumber = this.finalOrderService.processOrder(basketId, clientExtraOrderInfo, basket.getTotalValue());

        // Only when a successful confirmation of order, then we change the http cookie session value
        httpSession.setAttribute("totalOrdersCount", GlobalVariablesEventServices.totalNumberOfOrders);

        return "redirect:/users/order/" + orderNumber + "/details";
    }

    //    Display One Order details
    @GetMapping("/users/order/{orderNumber}/details")
    public String viewOrderDetails(Model model, @PathVariable String orderNumber, @AuthenticationPrincipal AppUser user) {
        Long userId = this.finalOrderService.getOrderByOrderNumber(orderNumber).getUser().getId();

        List<String> roles = user.getAuthorities().stream()
                .map(Object::toString).toList();

        if ((orderNumber != null && !Objects.equals(user.getId(), userId))
                && !(roles.contains("ROLE_ADMIN") || roles.contains("ROLE_EMPLOYEE_SALES"))) {
            throw new OrderForbiddenException(String.format("You do not have an authorization for a final order of user with id %d", userId));
        }

        OneOrderDetailsViewModel orderDetailsViewModel = new OneOrderDetailsViewModel();

        //!!! checking also if the order number exists or not
        FinalOrderEntity finalOrderEntity = this.finalOrderService.getOrderByOrderNumber(orderNumber);
        orderDetailsViewModel
                .setOrderNumber(finalOrderEntity.getOrderNumber())
                .setDeliveryAddress(finalOrderEntity.getExtraInfoForCurrentOrder().getDeliveryAddress())
                .setPhoneNumber(finalOrderEntity.getExtraInfoForCurrentOrder().getPhoneNumber())
                .setExtraInfo(finalOrderEntity.getExtraInfoForCurrentOrder().getExtraNotes())
                .setTotalTotal(finalOrderEntity.getTotalTotal());

        List<ItemQuantityInOrderEntity> productQuantities = this.finalOrderService.getProductQuantitiesFromOrderByOrderNumber(orderNumber);
        List<OneItemInOrderViewModel> itemsInOrder = new ArrayList<>();
        for (ItemQuantityInOrderEntity productQuantity : productQuantities) {
            OneItemInOrderViewModel oneItem = new OneItemInOrderViewModel();
            int boughtQuantity = productQuantity.getBoughtQuantity();
            BigDecimal sellingPricePerUnit = productQuantity.getItem().getSellingPrice();

            oneItem
                    .setType(productQuantity.getItem().getType())
                    .setModel(productQuantity.getItem().getModel())
                    .setQuantity(boughtQuantity)
                    .setPricePerUnit(sellingPricePerUnit)
                    .setSellingPriceForAllQuantity(sellingPricePerUnit.multiply(BigDecimal.valueOf(boughtQuantity)))
                    .setPhotoUrl(productQuantity.getItem().getPhoto() != null
                            ? productQuantity.getItem().getPhoto().getUrl()
                            : "");

            itemsInOrder.add(oneItem);
        }

        orderDetailsViewModel.setItems(itemsInOrder);

        if (!model.containsAttribute("order")) {
            model.addAttribute("order", orderDetailsViewModel);
        }

        return "/customer/OneOrder-details";
    }

    //One page where the orders of a client will be loaded   or  if SALES or ADMIN user then all orders will be loaded
    @GetMapping("/users/order/vieworders")
    public String viewOrders(Model model, @AuthenticationPrincipal AppUser user) {
        Long userId = user.getId();

        List<String> roles = user.getAuthorities().stream()
                .map(Object::toString).toList();

        //true if the user is admin or sales
        boolean adminOrSalesUser = roles.contains("ROLE_ADMIN") || roles.contains("ROLE_EMPLOYEE_SALES");

        if (!model.containsAttribute("adminOrSalesUser")) {
            model.addAttribute("adminOrSalesUser", adminOrSalesUser);
        }

        return "/customer/view-orders";
    }

    private Long isObjectIdANumber(String objectId, String comment) {
        final Long userLongId;
        try {
            userLongId = Long.parseLong(objectId);
        } catch (Exception e) {
            throw new ObjectIdNotANumberException(String.format("%s is not a %s number!", objectId, comment));
        }
        return userLongId;
    }

    private void isBasketIdAValidExistingNumber(Long basketId) {
        if (basketId <= 0 || basketId > basketService.getCountOfBaskets()) {
            throw new ObjectIdNotANumberException(String.format("%s is not a valid existing %s number!", basketId, "basketId"));
        }
    }

    private void doesUserHasAccessToBasket(AppUser user, Long userId) {
        if (userId != null && !Objects.equals(user.getId(), userId)) {
            throw new BasketForbiddenException(String.format("You do not have authorization to confirm the basket of user with id %d into a final order", userId));
        }
    }
}



