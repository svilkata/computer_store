package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.repository.orders.BasketRepository;
import bg.softuni.computerStore.repository.orders.QuantitiesItemsInBasketRepository;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.repository.users.UserRepository;
import bg.softuni.computerStore.service.*;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WithMockUser(username = "customer", roles = {"CUSTOMER"})
class RestBasketControllerTest {
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
    private AllItemsRepository allItemsRepository;

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
    void getBasketWithAllItemsTestSuccessfull() throws Exception {
        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");
        Long basketAndUserId = customer.get().getId();

        //itemIds 6 and 7
        this.mockMvc.perform(get("/users/basket/viewitems/{bId}", basketAndUserId)
//                        .param("bId", String.valueOf(basketAndUserId))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items.[0].model", is("ASUS PN51 Mini - 90MR00K1-M00800_16GBSODIMM_512NVMESSD_W10P")))
                .andExpect(jsonPath("$.items.[1].model", is("Lenovo IdeaCentre 5 Tower - 90RW005VRI")));
    }

    @Test
    void getBasketWithAllItemsTestWhenBasketIdIsNotANumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/basket/viewitems/" + "dqewd")
                        .with(csrf()))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getBasketWithAllItemsTestWhenBasketIdANumberWhichDoesNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/basket/viewitems/" + -5)
                        .with(csrf()))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void getBasketWithAllItemsTestWhenBasketForbiddenException() throws Exception {
        //Act and Assert
        //customerId == basketId =  4, and we test with 1

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/basket/viewitems/" + 1)
                        .with(csrf()))
                .andExpect(view().name("errors/basket-forbidden"))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void changeOneItemQuantityInBasketTestSuccessfull() throws Exception {
        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");
        Long basketAndUserId = customer.get().getId();

        //itemIds 6 - 1 quantity and 7 - 1 quantity
        this.mockMvc.perform(get("/users/basket/changeOneItemQuantityInBasket/{bId}", basketAndUserId)
                        .queryParam("itemId", 6 + "")
                        .queryParam("newQuantity", 2 + "")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items", hasSize(2)))
                .andExpect(jsonPath("$.items.[0].model", is("ASUS PN51 Mini - 90MR00K1-M00800_16GBSODIMM_512NVMESSD_W10P")))
                .andExpect(jsonPath("$.items.[0].quantity", is(2)))
                .andExpect(jsonPath("$.items.[1].model", is("Lenovo IdeaCentre 5 Tower - 90RW005VRI")))
                .andExpect(jsonPath("$.items.[1].quantity", is(1)));
    }

    @Test
    void changeOneItemQuantityInBasketTestWhenBasketIdANumberWhichDoesNotExist() throws Exception {
        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");
        Long basketAndUserId = customer.get().getId();

        //itemIds 6 - 1 quantity and 7 - 1 quantity
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/basket/changeOneItemQuantityInBasket/{bId}", -5)
                        .queryParam("itemId", 6 + "")
                        .queryParam("newQuantity", 1 + "")
                        .with(csrf()))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void changeOneItemQuantityInBasketTestWhenBasketForbiddenException() throws Exception {
        Optional<UserEntity> customer = this.userRepository.findByUsername("customer");
        Long basketAndUserId = customer.get().getId();

        //itemIds 6 - 1 quantity and 7 - 1 quantity
        //we are logged with user 4, and trying to access with user 1
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/users/basket/changeOneItemQuantityInBasket/{bId}", 1)
                        .queryParam("itemId", 6 + "")
                        .queryParam("newQuantity", 1 + "")
                        .with(csrf()))
                .andExpect(view().name("errors/basket-forbidden"))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void removeOneItemFromBasketTestWhenBasketIdANumberWhichDoesNotExist() throws Exception {
        //itemIds 6 - 1 quantity and 7 - 1 quantity
        this.mockMvc.perform(get("/users/basket/removeOneItemFromBasket/{bId}", -555)
                        .queryParam("itemId", 6 + "")
                        .with(csrf()))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void removeOneItemFromBasketTestWhenBasketForbiddenException() throws Exception {
        //itemIds 6 - 1 quantity and 7 - 1 quantity
        //we are logged with user 4, and trying to access with user 1
        this.mockMvc.perform(get("/users/basket/removeOneItemFromBasket/{bId}", 1)
                        .queryParam("itemId", 6 + "")
                        .with(csrf()))
                .andExpect(view().name("errors/basket-forbidden"))
                .andExpect(model().attributeExists("item"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addItemToBasketTestSuccessfull() throws Exception {
        //itemIds 6 and 7
        //we add item 4 in the basket
        this.mockMvc.perform(get("/users/basket/additemtobasket/{itmId}", 4)
                        .with(csrf()))
                .andExpect(status().isAccepted());
    }

    @Test
    void addItemToBasketTestItemAlreadyAddedInBasket() throws Exception {
        //itemIds 6 and 7
        //we try to add item 6 in the basket
        this.mockMvc.perform(get("/users/basket/additemtobasket/{itmId}", 6)
                        .with(csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addItemToBasketTestTryingToAddItemWithZeroQuantityInBasket() throws Exception {
        //itemIds 6 and 7
        ItemEntity itemEntity = this.allItemsRepository.findById(5L).get().setCurrentQuantity(0);
        this.allItemsRepository.save(itemEntity);
        //we try to add itemId 5 in the basket, but it has zero quantity
        this.mockMvc.perform(get("/users/basket/additemtobasket/{itmId}", 5)
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }
}