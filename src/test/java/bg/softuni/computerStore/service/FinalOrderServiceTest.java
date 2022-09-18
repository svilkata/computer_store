package bg.softuni.computerStore.service;

import bg.softuni.computerStore.exception.OrderNotFoundException;
import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;
import bg.softuni.computerStore.repository.users.UserRepository;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@WithMockUser(username = "customer", roles = {"CUSTOMER"})
class FinalOrderServiceTest {
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
        loginUser("admin");

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
    void processOrderTest() {
        //Test is made when initializing the default orders
    }

    @Test
    void confirmOrderByStoreTest() {
        List<FinalOrderEntity> allOrdersByUserId = this.finalOrderService.getAllOrdersByUserId(2L);
        FinalOrderEntity finalOrderEntity = allOrdersByUserId.get(0);

        this.finalOrderService.confirmOrderByStore(finalOrderEntity.getOrderNumber());
    }

    @Test
    void markOrderAsDeliveredTest() {
        List<FinalOrderEntity> allOrdersByUserId = this.finalOrderService.getAllOrdersByUserId(2L);
        FinalOrderEntity finalOrderEntity = allOrdersByUserId.get(0);

        this.finalOrderService.markOrderAsDelivered(finalOrderEntity.getOrderNumber());
    }

    @Test
    void getAllOrdersEagerTest() {
        this.finalOrderService.getAllOrdersEager();
    }

    @Test
    void getAllOrdersLazyTest() {
        this.finalOrderService.getAllOrdersLazy();
    }

    @Test
    void findIQOUUIDPrimaryTest() {
        List<FinalOrderEntity> allOrdersByUserId = this.finalOrderService.getAllOrdersByUserId(2L);
        FinalOrderEntity finalOrderEntity = allOrdersByUserId.get(0);

        this.finalOrderService.findIQOUUIDPrimary(finalOrderEntity.getId());
    }

    @Test
    void getOrderByOrderNumberTestSuccessfull() {
        List<FinalOrderEntity> allOrdersByUserId = this.finalOrderService.getAllOrdersByUserId(2L);
        FinalOrderEntity finalOrderEntity = allOrdersByUserId.get(0);

        this.finalOrderService.getOrderByOrderNumber(finalOrderEntity.getOrderNumber());
    }

    @Test
    void getOrderByOrderNumberTestOrderNotFoundException() {
        //Act and Assert
        assertThrows(OrderNotFoundException.class,
                () -> this.finalOrderService.getOrderByOrderNumber("dwqdascwqfqcveqfqcsa"));
    }

    @Test
    void getProductQuantitiesFromOrderByOrderNumberTest() {
        List<FinalOrderEntity> allOrdersByUserId = this.finalOrderService.getAllOrdersByUserId(2L);
        FinalOrderEntity finalOrderEntity = allOrdersByUserId.get(0);

        this.finalOrderService.getProductQuantitiesFromOrderByOrderNumber(finalOrderEntity.getOrderNumber());
    }

    @Test
    void getAllOrdersByUserIdTest() {
        List<FinalOrderEntity> allOrdersByUserId = this.finalOrderService.getAllOrdersByUserId(2L);
    }

    @Test
    void searchAllOrdersLazyByOrderNumberTest() {
        List<FinalOrderEntity> allOrdersByUserId = this.finalOrderService.getAllOrdersByUserId(2L);
        FinalOrderEntity finalOrderEntity = allOrdersByUserId.get(0);

        this.finalOrderService.searchAllOrdersLazyByOrderNumber(finalOrderEntity.getOrderNumber());
    }

    @Test
    void searchAllOrdersByUserIdAndOrderNumberTest() {
        List<FinalOrderEntity> allOrdersByUserId = this.finalOrderService.getAllOrdersByUserId(2L);
        FinalOrderEntity finalOrderEntity = allOrdersByUserId.get(0);

        this.finalOrderService.searchAllOrdersByUserIdAndOrderNumber(
                finalOrderEntity.getUser().getId(), finalOrderEntity.getOrderNumber());
    }
}