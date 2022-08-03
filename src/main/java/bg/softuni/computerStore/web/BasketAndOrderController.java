package bg.softuni.computerStore.web;

import bg.softuni.computerStore.exception.BasketIdForbiddenException;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.model.binding.order.ClientOrderExtraInfoGetViewModel;
import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import bg.softuni.computerStore.model.entity.orders.ItemQuantityInOrderEntity;
import bg.softuni.computerStore.model.view.order.OneBasketViewModel;
import bg.softuni.computerStore.model.view.order.OneItemInOrderViewModel;
import bg.softuni.computerStore.model.view.order.OneOrderDetailsViewModel;
import bg.softuni.computerStore.model.view.order.OneOrderInManyOrdersViewModel;
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
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
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

        //Creation order is successfull, we start creating the order
        String orderNumber = this.finalOrderService.processOrder(basketId, clientExtraOrderInfo, basket.getTotalValue());

        return "redirect:/users/vieworders/" + orderNumber + "/details";
    }

    //    Display One Order
    @GetMapping("/users/vieworders/{orderNumber}/details")
    public String viewOrderDetails(Model model, @PathVariable String orderNumber) {
        OneOrderDetailsViewModel orderDetailsViewModel = new OneOrderDetailsViewModel();

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
                    .setPhotoUrl(productQuantity.getItem().getPhoto().getUrl());

            itemsInOrder.add(oneItem);
        }

        orderDetailsViewModel.setItems(itemsInOrder);

        if (!model.containsAttribute("order")) {
            model.addAttribute("order", orderDetailsViewModel);
        }

        return "/customer/OneOrder-details";
    }

    //One page where the orders of a client will be loaded or all orders if SALES or ADMIN user
    @GetMapping("/users/vieworders")
    public String viewOrders(Model model, @AuthenticationPrincipal AppUser user) {
        Long userId = user.getId();

        List<String> roles = user.getAuthorities().stream()
                .map(Object::toString).toList();

        List<OneOrderInManyOrdersViewModel> ordersViewModels = new ArrayList<>();

        //Get list of all orders if ADMIN or SALES employee
        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_EMPLOYEE_SALES")) {
            List<FinalOrderEntity> allOrdersLazy = this.finalOrderService.getAllOrdersLazy();
            setAllOrdersView(ordersViewModels, allOrdersLazy);
        } else if (roles.contains("ROLE_CUSTOMER")){
            //Get orders for the current client user only
            List<FinalOrderEntity> allCurrentUserOrders = this.finalOrderService.getAllOrdersByUserId(userId);
            setAllOrdersView(ordersViewModels, allCurrentUserOrders);
        }

        if (ordersViewModels.isEmpty()) {

        }

        if (!model.containsAttribute("orders")) {
            model.addAttribute("orders", ordersViewModels);
        }

        return "/customer/view-orders";
    }

    //to do it with modelMapper!!!
    private void setAllOrdersView(List<OneOrderInManyOrdersViewModel> ordersViewModels, List<FinalOrderEntity> allOrdersLazy) {
        for (FinalOrderEntity finalOrder : allOrdersLazy) {
            OneOrderInManyOrdersViewModel oneOrderInManyOrdersViewModel = new OneOrderInManyOrdersViewModel();
            oneOrderInManyOrdersViewModel
                    .setUsername(finalOrder.getUser().getUsername())
                    .setOrderNumber(finalOrder.getOrderNumber())
                    .setCreatedAt(finalOrder.getCreationDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .setTotalItemsInOrder(finalOrder.getCountTotalProducts())
                    .setTotalValue(finalOrder.getTotalTotal())
                    .setOrderStatus(finalOrder.getStatus().toString());


            ordersViewModels.add(oneOrderInManyOrdersViewModel);
        }
    }


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
