package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.order.ClientOrderExtraInfoGetViewModel;
import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.repository.users.UserRepository;
import bg.softuni.computerStore.service.*;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class BasketAndOrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PictureService pictureService;
    @Autowired
    private ComputerService computerService;
    @Autowired
    private MonitorService monitorService;
    @Autowired
    private LaptopService laptopService;

    @Autowired
    private UserDetailsService appUserDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BasketService basketService;
    @Autowired
    private FinalOrderService finalOrderService;

    @BeforeEach
    void setUp() {
        pictureService.init();
        computerService.init(); //7 items
        monitorService.init();  //3 items
        laptopService.init();  //1 item

        this.userService.init();

        this.basketService.init();
        this.finalOrderService.init();
    }

    private void loginUser(String username) {
        //The login process of user with username "admin"  doing it below
        UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.
                getContext().
                setAuthentication(authentication);
    }


    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewBasketWithItemsTestSuccessfull() throws Exception {
        loginUser("customer");
        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/basket/" + customer.get().getId())
                        .with(csrf()))
                .andExpect(view().name("/customer/OneBasket-items"))
                .andExpect(model().attributeExists("basketId"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewBasketWithItemsTestWhenWhenUserIdIsNotANumber() throws Exception {
        //Act and Assert
        loginUser("customer");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/basket/" + "dqewd")
                        .with(csrf()))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewBasketWithItemsTestWhenUserIdANumberWhichDoesNotExist() throws Exception {
        //Act and Assert
        loginUser("customer");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/basket/" + -5)
                        .with(csrf()))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewBasketWithItemsTestBasketForbiddenException() throws Exception {
        //Act and Assert
        //customerId = 4, and we test with 1
        loginUser("customer");
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/basket/" + 1)
                        .with(csrf()))
                .andExpect(view().name("errors/basket-forbidden"))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewOrderWithItemsAndAddAddressTestSuccessfull() throws Exception {
        loginUser("customer");
        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");
        //userId is now always also a basketId

        //   /users/order/basketId
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/order/" + customer.get().getId())
                        .with(csrf()))
                .andExpect(view().name("/customer/OneOrder-confirm"))
                .andExpect(model().attributeExists("basket", "clientOrderExtraInfo", "userId", "basketId"))  //all attributes to exist
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewOrderWithItemsAndAddAddressTestWhenUserIdIsNotANumber() throws Exception {
        loginUser("customer");

        //   /users/order/basketId
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/order/" + "qefqcs")
                        .with(csrf()))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewOrderWithItemsAndAddAddressTestWhenBasketIdANumberWhichDoesNotExist() throws Exception {
        loginUser("customer");

        //   /users/order/basketId
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/order/" + -3)
                        .with(csrf()))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewOrderWithItemsAndAddAddressTestBasketForbiddenException() throws Exception {
        loginUser("customer");

        // customerId = 4, and we test with 1
        //   /users/order/basketId
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/order/" + 1)
                        .with(csrf()))
                .andExpect(view().name("errors/basket-forbidden"))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewOrderWithItemsAndAddAddressConfirmTestSuccessfull() throws Exception {
        loginUser("customer");

        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");
        ClientOrderExtraInfoGetViewModel clientOrderExtraInfoGetViewModel = new ClientOrderExtraInfoGetViewModel();
        clientOrderExtraInfoGetViewModel
                .setDeliveryAddress("Pragka prolet 18")
                .setPhoneNumber("08999888777666")
                .setExtraNotes("bla bla bla");

        //userId is now always also a basketId
        String pathVariableBasketId = String.valueOf(customer.get().getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/order/{bId}", pathVariableBasketId)
                        .param("bId", pathVariableBasketId)
                        .param("deliveryAddress", clientOrderExtraInfoGetViewModel.getDeliveryAddress())
                        .param("phoneNumber", clientOrderExtraInfoGetViewModel.getPhoneNumber())
                        .param("extraNotes", clientOrderExtraInfoGetViewModel.getExtraNotes())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewOrderWithItemsAndAddAddressConfirmTestBindingResultHasErrors() throws Exception {
        loginUser("customer");

        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");
        ClientOrderExtraInfoGetViewModel clientOrderExtraInfoGetViewModel = new ClientOrderExtraInfoGetViewModel();
        clientOrderExtraInfoGetViewModel
                .setDeliveryAddress("Pragka prolet 18")
                .setPhoneNumber("08999888777666")
                .setExtraNotes("bla bla bla");

        //userId is now always also a basketId
        String pathVariableBasketId = String.valueOf(customer.get().getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/order/{bId}", pathVariableBasketId)
                        .param("bId", pathVariableBasketId)
                        .param("deliveryAddress", "")
                        .param("phoneNumber", clientOrderExtraInfoGetViewModel.getPhoneNumber())
                        .param("extraNotes", clientOrderExtraInfoGetViewModel.getExtraNotes())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("clientExtraOrderInfo", "basket", "userId", "basketId"))
                .andExpect(redirectedUrl("/users/order/" + pathVariableBasketId));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewOrderWithItemsAndAddAddressConfirmTestWhenBasketIdANumberWhichDoesNotExist() throws Exception {
        loginUser("customer");

        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");
        ClientOrderExtraInfoGetViewModel clientOrderExtraInfoGetViewModel = new ClientOrderExtraInfoGetViewModel();
        clientOrderExtraInfoGetViewModel
                .setDeliveryAddress("Pragka prolet 18")
                .setPhoneNumber("08999888777666")
                .setExtraNotes("bla bla bla");

        //userId is now always also a basketId
        String pathVariableBasketId = String.valueOf(customer.get().getId());

        mockMvc.perform(MockMvcRequestBuilders.post("/users/order/{bId}", -5 + "")
                        .param("bId", -5 + "")
                        .param("deliveryAddress", clientOrderExtraInfoGetViewModel.getDeliveryAddress())
                        .param("phoneNumber", clientOrderExtraInfoGetViewModel.getPhoneNumber())
                        .param("extraNotes", clientOrderExtraInfoGetViewModel.getExtraNotes())
                        .with(csrf()))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewOrderWithItemsAndAddAddressConfirmTestWhenBasketForbiddenException() throws Exception {
        loginUser("customer");

        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");
        ClientOrderExtraInfoGetViewModel clientOrderExtraInfoGetViewModel = new ClientOrderExtraInfoGetViewModel();
        clientOrderExtraInfoGetViewModel
                .setDeliveryAddress("Pragka prolet 18")
                .setPhoneNumber("08999888777666")
                .setExtraNotes("bla bla bla");

        //userId is now always also a basketId
        String pathVariableBasketId = String.valueOf(customer.get().getId());
        // customerId = 4, and we test with 2

        mockMvc.perform(MockMvcRequestBuilders.post("/users/order/{bId}", 2)
                        .param("bId", 2 + "")
                        .param("deliveryAddress", clientOrderExtraInfoGetViewModel.getDeliveryAddress())
                        .param("phoneNumber", clientOrderExtraInfoGetViewModel.getPhoneNumber())
                        .param("extraNotes", clientOrderExtraInfoGetViewModel.getExtraNotes())
                        .with(csrf()))
                .andExpect(view().name("errors/basket-forbidden"))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().is4xxClientError());
    }


    @Test
    @WithMockUser(username = "sales", roles = {"CUSTOMER", "EMPLOYEE_SALES"})
    void viewOrderDetailsTestSuccessfull() throws Exception {
        loginUser("sales");

        Optional<UserEntity> sales = this.userRepository.findByUsername("sales");
        Map<Long, String> ordersTemp = new HashMap<>();

        List<FinalOrderEntity> allOrdersEager = this.finalOrderService.getAllOrdersEager();
        allOrdersEager.forEach(o -> ordersTemp.put(o.getUser().getId(), o.getOrderNumber()));
        String testOrderNumber = ordersTemp.get(sales.get().getId());

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/order/{orderNumber}/details", testOrderNumber)
                        .with(csrf()))
                .andExpect(view().name("/customer/OneOrder-details"))
                .andExpect(model().attributeExists("order"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "sales", roles = {"CUSTOMER", "EMPLOYEE_SALES"})
    void viewOrderDetailsTestWhenOrderDoesNotExist() throws Exception {
        loginUser("sales");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/order/{orderNumber}/details", "feqwffqfq")
                        .with(csrf()))
                .andExpect(view().name("errors/order-notfound"))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "purchase", roles = {"CUSTOMER", "EMPLOYEE_PURCHASES"})
    void viewOrderDetailsTestOrderForbiddenException() throws Exception {
        loginUser("purchase");

        Map<Long, String> ordersTemp = new HashMap<>();

        List<FinalOrderEntity> allOrdersEager = this.finalOrderService.getAllOrdersEager();
        allOrdersEager.forEach(o -> ordersTemp.put(o.getUser().getId(), o.getOrderNumber()));
        String testOrderNumber = ordersTemp.get(3L);

        //we test to access order details for user with userId 3, but we have logged with userId 2
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/order/{orderNumber}/details", testOrderNumber)
                        .with(csrf()))
                .andExpect(view().name("errors/order-forbidden"))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewOrdersTest() throws Exception {
        loginUser("customer");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/order/vieworders")
                        .with(csrf()))
                .andExpect(view().name("/customer/view-orders"))
                .andExpect(model().attributeExists("adminOrSalesUser"))
                .andExpect(status().isOk());
    }
}