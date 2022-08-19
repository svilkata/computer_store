package bg.softuni.computerStore.service;

import bg.softuni.computerStore.repository.orders.BasketRepository;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
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
//@DataJpaTest
//@AutoConfigureTestDatabase
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

//    @Test
//    void viewAllItemsFromOneBasket() {
//    }
//
//    @Test
//    void getUserIdByBasketId() {
//    }
//
//    @Test
//    void changeOrderedQuantity() {
//    }
//
//    @Test
//    void removeOneItemFromBasket() {
//    }
//
//    @Test
//    void getItemFromItemQuantityInBasketEntityByBasketItem() {
//    }
//
//    @Test
//    void readOneBasket() {
//    }
//
//    @Test
//    void getOneBasket() {
//    }
//
//    @Test
//    void getBaskeIdByUserId() {
//    }
//
//    @Test
//    void addBasketForRegisteredUser() {
//    }
//
//    @Test
//    void resetOneBasketWhen20MinutesPassed() {
//    }
//
//    @Test
//    void getAllBasketsCreatedMoreThan20MinutesAgo() {
//    }
}