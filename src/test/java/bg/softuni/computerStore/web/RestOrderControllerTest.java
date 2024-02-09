package bg.softuni.computerStore.web;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
class RestOrderControllerTest {
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
        UserDetails userDetails =
                appUserDetailsService.loadUserByUsername(username);

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
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void viewOrdersAndSearchOrdersTestSuccessfullAllOrders() throws Exception {
        loginUser("admin");

        this.mockMvc.perform(get("/users/order/viewordersrest")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewOrdersAndSearchOrdersTestOnlyCustomerOrders() throws Exception {
        loginUser("customer");

        this.mockMvc.perform(get("/users/order/viewordersrest")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void changeOrderStatusWithSearchIncludedTestWrongCustomerCanNotUpdateAnyOrder() throws Exception {
        loginUser("customer");

        Optional<UserEntity> sales = this.userRepository.findByUsername("sales");
        Map<Long, String> ordersTemp = new HashMap<>();

        List<FinalOrderEntity> allOrdersEager = this.finalOrderService.getAllOrdersEager();
        allOrdersEager.forEach(o -> ordersTemp.put(o.getUser().getId(), o.getOrderNumber()));
        String testOrderNumber = ordersTemp.get(sales.get().getId());

        this.mockMvc.perform(get("/users/order/changestatus/{orderNumber}", testOrderNumber)
                        .queryParam("orderStatus", "CONFIRMED_BY_CUSTOMER")
                        .queryParam("search", testOrderNumber.substring(0,6))
                        .with(csrf()))
                .andExpect(view().name("errors/order-forbidden"))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "sales", roles = {"EMPLOYEE_SALES"})
    void changeOrderStatusWithSearchIncludedTestAuthorizedUserButWrongOrder() throws Exception {
        loginUser("sales");

        this.mockMvc.perform(get("/users/order/changestatus/{orderNumber}", "fqefasqwdqddwqwdqwd")
                        .queryParam("orderStatus", "CONFIRMED_BY_CUSTOMER")
                        .queryParam("search", "")
                        .with(csrf()))
                .andExpect(view().name("errors/order-notfound"))
            .andExpect(model().attributeExists("item"))
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "sales", roles = {"EMPLOYEE_SALES"})
    void changeOrderStatusWithSearchIncludedTestSuccessfullAuthorizedUserCorrectOrderNoSearch() throws Exception {
        loginUser("sales");
        Optional<UserEntity> sales = this.userRepository.findByUsername("sales");
        Map<Long, String> ordersTemp = new HashMap<>();

        List<FinalOrderEntity> allOrdersEager = this.finalOrderService.getAllOrdersEager();
        allOrdersEager.forEach(o -> ordersTemp.put(o.getUser().getId(), o.getOrderNumber()));
        String testOrderNumber = ordersTemp.get(sales.get().getId());

        this.mockMvc.perform(get("/users/order/changestatus/{orderNumber}", testOrderNumber)
                        .queryParam("orderStatus", "CONFIRMED_BY_STORE")
                        .queryParam("search", "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @WithMockUser(username = "sales", roles = {"EMPLOYEE_SALES"})
    void changeOrderStatusWithSearchIncludedTestSuccessfullAuthorizedUserCorrectOrderWithSearch() throws Exception {
        loginUser("sales");
        Optional<UserEntity> sales = this.userRepository.findByUsername("sales");
        Map<Long, String> ordersTemp = new HashMap<>();

        List<FinalOrderEntity> allOrdersEager = this.finalOrderService.getAllOrdersEager();
        allOrdersEager.forEach(o -> ordersTemp.put(o.getUser().getId(), o.getOrderNumber()));
        String testOrderNumber = ordersTemp.get(sales.get().getId());

        this.mockMvc.perform(get("/users/order/changestatus/{orderNumber}", testOrderNumber)
                        .queryParam("orderStatus", "DELIVERED")
                        .queryParam("search", testOrderNumber.substring(0, 8))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void searchOrderByOrderNumberTest() throws Exception {
        loginUser("customer");

        this.mockMvc.perform(get("/users/order/searchorders")
                        .queryParam("search", "cdafda")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}

