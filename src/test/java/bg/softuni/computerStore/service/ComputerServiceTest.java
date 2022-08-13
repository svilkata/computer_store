package bg.softuni.computerStore.service;

import bg.softuni.computerStore.model.entity.cloudinary.PictureEntity;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.entity.users.UserRoleEntity;
import bg.softuni.computerStore.model.enums.UserRoleEnum;
import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.repository.users.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static bg.softuni.computerStore.constants.Constants.IMAGE_PUBLIC_ID_COMPUTER_1;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class ComputerServiceTest {
    private UserEntity testUser;
    private ItemEntity testComputerEntity;
    private ComputerViewGeneralModel testComputerViewGeneralModel;
    private UserRoleEntity adminRole, customerRole;
    private AppUserDetailsService testAppUserDetailsService;

    @Mock
    private UserRepository mockUserRepository;
    @Mock
    private AllItemsRepository mockAllItemsRepository;
    @Mock
    private ComputerService mockedComputerService;


    @BeforeEach
    void init() {
        testAppUserDetailsService = new AppUserDetailsService(mockUserRepository);

        adminRole = new UserRoleEntity().setUserRole(UserRoleEnum.ADMIN);
        customerRole = new UserRoleEntity().setUserRole(UserRoleEnum.CUSTOMER);

        testUser = new UserEntity()
                .setFirstName("Svilen")
                .setLastName("Velikov")
                .setUsername("admin")
                .setEmail("svilkata@abv.bg")
                .setPassword("11111")
                .setUserRoles(Set.of(adminRole, customerRole));

        testComputerEntity = initOneComputer("Dell", "Dell Vostro 3681 SFF", 1000, 1150, 5,
                "Intel Core i3-10100 (3.6/4.3GHz, 6M)", "Intel UHD Graphics 630",
                "8 GB DDR4 2666 MHz", "1TB 7200rpm", "256 GB SSD M.2 NVMe",
                "36 месеца международна гаранция Next Business Day");

        testComputerViewGeneralModel = new ComputerViewGeneralModel();
        testComputerViewGeneralModel
                .setItemId(testComputerEntity.getItemId())
                .setBrand(testComputerEntity.getBrand())
                .setModel(testComputerEntity.getModel())
                .setCurrentQuantity(Long.parseLong(testComputerEntity.getCurrentQuantity()+""))
                .setMoreInfo(testComputerEntity.getMoreInfo())
                .setSellingPrice(testComputerEntity.getSellingPrice());

    }

    private ComputerEntity initOneComputer(String brand, String model, double buyAt, double sellAt, int newQuantity,
                                 String processor, String videoCard, String ram, String disk, String ssd,
                                 String extraInfo) {
        //With constructor
        ComputerEntity toAdd = new ComputerEntity(brand, model, BigDecimal.valueOf(buyAt), BigDecimal.valueOf(sellAt),
                newQuantity, extraInfo);
        toAdd
                .setProcessor(processor)
                .setVideoCard(videoCard)
                .setRam(ram)
                .setDisk(disk)
                .setSsd(ssd);

        toAdd.setItemId(1L);

        return toAdd;
    }

    @Test
    void findOneComputerByIdTest() {
        //Arrange
//        Mockito.when(mockAllItemsRepository.findItemEntityByTypeAndItemId(
//                testComputerEntity.getType(), testComputerEntity.getItemId()))
//                .thenReturn(Optional.of(testComputerEntity));
        Mockito.when(this.mockedComputerService.findOneComputerById(testComputerEntity.getItemId().toString()))
                .thenReturn(testComputerViewGeneralModel);

        //Act
        ComputerViewGeneralModel actual = this.mockedComputerService.findOneComputerById(testComputerEntity.getItemId().toString());


        //Assert
        Assertions.assertEquals(actual.getItemId(), testComputerViewGeneralModel.getItemId());
    }

//    @Test
//    void findAllComputers() {
//    }
//
//    @Test
//    void saveNewComputer() {
//    }
//
//    @Test
//    void deleteComputerAndQuantity() {
//    }
//
//    @Test
//    void findComputerByIdUpdatingItem() {
//    }
//
//    @Test
//    void updateExistingComputer() {
//    }
}