package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import bg.softuni.computerStore.model.view.order.OneOrderInManyOrdersViewModel;
import bg.softuni.computerStore.service.FinalOrderService;
import bg.softuni.computerStore.user.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
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


        return ResponseEntity.ok(ordersViewModels);
    }

    @GetMapping("/users/order/changestatus/{orderNumber}")
    public String changeOrderStatus(){
        return null;
    }

    @GetMapping("/users/order/searchorders")
    public String searchOrderBy(){
        return null;
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

        //SORTED by the last created order - as each order is saved first, so we just reverse the collection
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
