package bg.softuni.computerStore.web;

import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        loginUser("customer");
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
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewBasketWithItemsTestSuccessfull() throws Exception {
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
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/basket/" + "dqewd")
                        .with(csrf()))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser(username = "customer", roles = {"CUSTOMER"})
    void viewBasketWithItemsTestWhenWhenUserIdANumberWhichDoesNotExist() throws Exception {
        //Act and Assert
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
        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/basket/" + 1)
                        .with(csrf()))
                .andExpect(view().name("errors/basket-forbidden"))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().is4xxClientError());
    }

//    @Test
//    void viewOrderWithItemsAndAddAddress() {
//    }
//
//    @Test
//    void viewOrderWithItemsAndAddAddressConfirm() {
//    }
//
//    @Test
//    void viewOrderDetails() {
//    }
//
//    @Test
//    void viewOrders() {
//    }
}