package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.repository.orders.BasketRepository;
import bg.softuni.computerStore.repository.orders.QuantitiesItemsInBasketRepository;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WithMockUser(username = "customer", roles = {"CUSTOMER"})
class RestBasketControllerTestExtraCases {
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

    @BeforeEach
    void setUp() {
        pictureService.init();
        computerService.init(); //7 items
        monitorService.init();  //3 items
        laptopService.init();  //1 item

        this.userService.init();

        this.basketService.init();

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
    void changeOneItemQuantityInBasketTestLastQuantitiesOfTheChangedItemLeft() throws Exception {
        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");
        Long basketAndUserId = customer.get().getId();

        //itemIds 6 - 1 quantity and 7 - 1 quantity
        this.mockMvc.perform(get("/users/basket/changeOneItemQuantityInBasket/{bId}", basketAndUserId)
                        .queryParam("itemId", 7 + "")
                        .queryParam("newQuantity", 2 + "")
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items.[0].model", is("ASUS PN51 Mini - 90MR00K1-M00800_16GBSODIMM_512NVMESSD_W10P")))
                .andExpect(jsonPath("$.items.[0].quantity", is(1)))
                .andExpect(jsonPath("$.items.[1].model", is("Lenovo IdeaCentre 5 Tower - 90RW005VRI")))
                .andExpect(jsonPath("$.items.[1].quantity", is(1)));
    }

    @Test
    void changeOneItemQuantityInBasketTestQuantityCanNotBeNegativeOrZero() throws Exception {
        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");
        Long basketAndUserId = customer.get().getId();

        //itemIds 6 - 1 quantity and 7 - 1 quantity
        this.mockMvc.perform(get("/users/basket/changeOneItemQuantityInBasket/{bId}", basketAndUserId)
                        .queryParam("itemId", 7 + "")
                        .queryParam("newQuantity", -3 + "")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items.[0].model", is("ASUS PN51 Mini - 90MR00K1-M00800_16GBSODIMM_512NVMESSD_W10P")))
                .andExpect(jsonPath("$.items.[0].quantity", is(1)))
                .andExpect(jsonPath("$.items.[1].model", is("Lenovo IdeaCentre 5 Tower - 90RW005VRI")))
                .andExpect(jsonPath("$.items.[1].quantity", is(1)));
    }
}