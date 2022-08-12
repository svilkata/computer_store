package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.entity.users.UserRoleEntity;
import bg.softuni.computerStore.model.enums.UserRoleEnum;
import bg.softuni.computerStore.repository.orders.BasketRepository;
import bg.softuni.computerStore.repository.orders.QuantitiesItemsInBasketRepository;
import bg.softuni.computerStore.repository.users.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

/*
@SpringBootTest
@AutoConfigureMockMvc
public class RestBasketControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BasketRepository testBasketRepository;
    @Autowired
    private QuantitiesItemsInBasketRepository testQuantitiesItemsInBasketRepository;
    @Autowired
    private UserRepository testUserRepository;

    private UserRoleEntity adminRole, customerRole;
    private UserEntity testUser;


    @BeforeEach
    void setUp(){
        adminRole = new UserRoleEntity().setUserRole(UserRoleEnum.ADMIN);
        customerRole = new UserRoleEntity().setUserRole(UserRoleEnum.CUSTOMER);

        testUser = new UserEntity()
                .setFirstName("Svilen")
                .setLastName("Velikov")
                .setUsername("admin")
                .setEmail("svilkata@abv.bg")
                .setPassword("11111")
                .setUserRoles(Set.of(adminRole, customerRole));

        this.testUserRepository.save(testUser);
    }

    @AfterEach
    void tearDown(){
        testUserRepository.deleteAll();
        testBasketRepository.deleteAll();
//        testQuantitiesItemsInBasketRepository.deleteAllByBasket_Id();
    }

    @Test
    void testGetBasketWithAllItems() {

    }

    private void initBasketItems(){

    }



}

 */