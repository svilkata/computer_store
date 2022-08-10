package bg.softuni.computerStore.web;

import bg.softuni.computerStore.exception.OrderForbiddenException;
import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import bg.softuni.computerStore.model.view.order.OneOrderInManyOrdersViewModel;
import bg.softuni.computerStore.service.FinalOrderService;
import bg.softuni.computerStore.user.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class RestOrderController {
    private final FinalOrderService finalOrderService;

    public RestOrderController(FinalOrderService finalOrderService) {
        this.finalOrderService = finalOrderService;
    }

    @GetMapping("/users/order/viewordersrest")
    public ResponseEntity<List<OneOrderInManyOrdersViewModel>> viewOrdersAndSearchOrders(@AuthenticationPrincipal AppUser user) {
        List<String> roles = user.getAuthorities().stream().map(Object::toString).toList();

        Long userId = user.getId();
        List<OneOrderInManyOrdersViewModel> ordersViewModels = new ArrayList<>();

        getRespectiveOrders(roles, ordersViewModels, userId);

        return ResponseEntity.ok(ordersViewModels);
    }

    @GetMapping("/users/order/changestatus/{orderNumber}")
    public ResponseEntity<List<OneOrderInManyOrdersViewModel>> changeOrderStatusWithSearchIncluded(
            @PathVariable String orderNumber, @RequestParam("orderStatus") String orderStatus,
            @RequestParam("search") String searchByOrderNumber, @AuthenticationPrincipal AppUser user) {

        List<String> roles = user.getAuthorities().stream().map(Object::toString).toList();

        if (!roles.contains("ROLE_ADMIN") && !roles.contains("ROLE_EMPLOYEE_SALES")) {
            throw new OrderForbiddenException("Users which do not have roles of ADMIN or of " +
                    "EMPLOYEE_SALES cannot change order status!");
        }

        //checking only if the order number exists or not
        this.finalOrderService.getOrderByOrderNumber(orderNumber);

        Long userId = user.getId();
        List<OneOrderInManyOrdersViewModel> ordersViewModels = new ArrayList<>();

        //updating the status of the order to CONFIRMED_BY_STORE
        if (orderStatus.equals("CONFIRMED_BY_STORE")) {
            this.finalOrderService.confirmOrderByStore(orderNumber);
        }

        //updating the status of the order to DELIVERED
        if (orderStatus.equals("DELIVERED")) {
            this.finalOrderService.markOrderAsDelivered(orderNumber);
        }

        if (searchByOrderNumber.equals("")) {
            getRespectiveOrders(roles, ordersViewModels, userId);
        } else {
            getRespectiveOrdersAndSearchCriteriaIncluded(searchByOrderNumber, roles, userId, ordersViewModels);
        }

        return ResponseEntity.ok(ordersViewModels);
    }


    @GetMapping("/users/order/searchorders")
    public ResponseEntity<List<OneOrderInManyOrdersViewModel>> searchOrderByOrderNumber(@RequestParam("search") String searchByOrderNumber,
                                                                                        @AuthenticationPrincipal AppUser user) {
        Long userId = user.getId();

        List<String> roles = user.getAuthorities().stream()
                .map(Object::toString).toList();

        List<OneOrderInManyOrdersViewModel> ordersViewModels = new ArrayList<>();

        getRespectiveOrdersAndSearchCriteriaIncluded(searchByOrderNumber, roles, userId, ordersViewModels);

        return ResponseEntity.ok(ordersViewModels);
    }

    private void getRespectiveOrdersAndSearchCriteriaIncluded(String searchByOrderNumber, List<String> roles, Long userId, List<OneOrderInManyOrdersViewModel> ordersViewModels) {
        //Get list of all orders if ADMIN or SALES employee, but with specific search criteria
        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_EMPLOYEE_SALES")) {
            List<FinalOrderEntity> allOrdersLazySearched = this.finalOrderService.searchAllOrdersLazyByOrderNumber(searchByOrderNumber);
            setAllOrdersView(ordersViewModels, allOrdersLazySearched);
        } else if (roles.contains("ROLE_CUSTOMER")) {
            //Get orders for the current client user only
            List<FinalOrderEntity> allCurrentUserOrdersSearched =
                    this.finalOrderService.searchAllOrdersByUserIdAndOrderNumber(userId, searchByOrderNumber);
            setAllOrdersView(ordersViewModels, allCurrentUserOrdersSearched);
        }
    }

    private void getRespectiveOrders(List<String> roles, List<OneOrderInManyOrdersViewModel> ordersViewModels, Long userId) {
        //Get list of all orders if ADMIN or SALES employee
        if (roles.contains("ROLE_ADMIN") || roles.contains("ROLE_EMPLOYEE_SALES")) {
            List<FinalOrderEntity> allOrdersLazy = this.finalOrderService.getAllOrdersLazy();
            setAllOrdersView(ordersViewModels, allOrdersLazy);
        } else if (roles.contains("ROLE_CUSTOMER")) {
            //Get orders for the current client user only
            List<FinalOrderEntity> allCurrentUserOrders = this.finalOrderService.getAllOrdersByUserId(userId);
            setAllOrdersView(ordersViewModels, allCurrentUserOrders);
        }
    }

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

        //TODO: SORTED by the last created order - as each order is saved first, so we just reverse the collection
        Collections.reverse(ordersViewModels);

//        if (!ordersViewModels.isEmpty()) {
//            ordersViewModels = ordersViewModels.stream()
//                    .sorted((f,s) -> {
//                        f.getCreatedAt().compareTo()
//                    })
//                    .collect(Collectors.toList());
//        }
    }
}
