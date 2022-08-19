package bg.softuni.computerStore.service;

import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.repository.orders.BasketRepository;
import bg.softuni.computerStore.repository.users.UserRepository;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@WithMockUser(username = "customer", roles = {"CUSTOMER"})
class BasketServiceTest {
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
//    @Autowired
//    private FinalOrderService finalOrderService;

    @BeforeEach
    void setUp() {
        pictureService.init();
        computerService.init(); //7 items
        monitorService.init();  //3 items
        laptopService.init();  //1 item

        this.userService.init();
        loginUser("admin");

        this.basketService.init();
//        this.finalOrderService.init();
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
    void resetOneBasketWhenFinalOrderConfirmedTestSuccessfull() {
        Long openBasketWithId = 1L;
        this.basketService.resetOneBasketWhenFinalOrderConfirmed(openBasketWithId);
    }

    @Test
    void addNewItemToBasketTestSuccessfull() {
        Long itemWIthItemIdForAddingToBasket = 8L;
        Long basketWithIdToAddItemIn = 4L;
        int result = this.basketService.addNewItemToBasket(itemWIthItemIdForAddingToBasket, basketWithIdToAddItemIn);

        Assertions.assertEquals(result, 1);
    }

    @Test
    void addNewItemToBasketTestItemHasZeroQuantity() {
        Long itemWithItemIdForAddingToBasketWithZeroQuantity = 7L;
        Long basketWithIdToAddItemIn = 4L;
        int result = this.basketService.addNewItemToBasket(itemWithItemIdForAddingToBasketWithZeroQuantity, basketWithIdToAddItemIn);

        Assertions.assertEquals(result, -2);
    }

    @Test
    void addNewItemToBasketTestWhenWeTryAddSecondTimeSameProduct() {
        //Arrange
        Long itemWithItemIdTwoTimes = 8L;
        Long basketWithIdToAddItemIn = 4L;
        this.basketService.addNewItemToBasket(itemWithItemIdTwoTimes, basketWithIdToAddItemIn);

        //Act
        int result = this.basketService.addNewItemToBasket(itemWithItemIdTwoTimes, basketWithIdToAddItemIn);

        //Assert
        Assertions.assertEquals(result, -1);
    }

    @Test
    void addNewItemToBasketTestSuccessfullWhenInitialBasketIsEmpty() {
        //Arrange
        Long itemWithItemId = 8L;
        Long basketEmptyWithIdToAddItemIn = 2L;
        this.basketService.resetOneBasketWhenFinalOrderConfirmed(basketEmptyWithIdToAddItemIn);

        //Act
        int result = this.basketService.addNewItemToBasket(itemWithItemId, basketEmptyWithIdToAddItemIn);

        //Assert
        Assertions.assertEquals(result, 1);
    }

    @Test
    void viewAllItemsFromOneBasketTest() {
        this.basketService.viewAllItemsFromOneBasket(1L);
    }

    @Test
    void getUserIdByBasketIdTestSuccessfull() {
        //For user 1, the basketId is 1
        Long userIdByBasketId = this.basketService.getUserIdByBasketId(1L);

        Assertions.assertEquals(1L, userIdByBasketId);
    }

    @Test
    void changeOrderedQuantityTestSuccessfull() {
        Long basketId = 1L;
        Long itemId = 2L;
        Long newQuantity = 3L;
        this.basketService.changeOrderedQuantity(basketId, itemId, newQuantity);
    }

    @Test
    void removeOneItemFromBasketTestNoMatterSuccessfullOrNot() {
        Long basketId = 1L;
        Long itemId = 2L;
        this.basketService.removeOneItemFromBasket(basketId, itemId);
    }

    @Test
    void readOneBasket() {
        BasketOrderEntity basketOrder = this.basketService.readOneBasket(1L);
        Assertions.assertEquals(basketOrder.getId(), 1L);
    }

    @Test
    void getOneBasketTest() {
        BasketOrderEntity basketOrder = this.basketService.getOneBasket(1L);
        Assertions.assertEquals(basketOrder.getId(), 1L);
    }

    @Test
    void getBaskeIdByUserIdTest() {
        Long baskeIdByUserId = this.basketService.getBaskeIdByUserId(1L);
        Assertions.assertEquals(baskeIdByUserId, 1L);
    }

    @Test
    void addBasketForRegisteredUserTest() {
        UserEntity userEntity = this.userRepository.findByUsername("purchase").orElseThrow();
        this.basketService.addBasketForRegisteredUser(userEntity);
    }

    @Test
    void resetOneBasketWhen20MinutesPassedTest() {
        this.basketService.resetOneBasketWhen20MinutesPassed(1L);
    }

    @Test
    void getAllBasketsCreatedMoreThan20MinutesAgoTest() {
        this.basketService.getAllBasketsCreatedMoreThan20MinutesAgo();
    }
}