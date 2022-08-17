package bg.softuni.computerStore.service;

import bg.softuni.computerStore.exception.ItemNotFoundException;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
class LaptopServiceTest {
    @Autowired
    private PictureService pictureService;
    @Autowired
    private LaptopService laptopService;
    @Autowired
    private AllItemsRepository allItemsRepository;

    @BeforeEach
    void setUp() {
        //do nothing for the moment
    }

//    @Test
//    void findOneLaptopByIdTestSuccessfull() {
//        //Act
//        LaptopViewGeneralModel expectedLaptopById = this.laptopService.findOneLaptopById(1 + "");
//
//        Optional<ItemEntity> resultItem = this.allItemsRepository.findById(1L);
//
//        //Assert
//        Assertions.assertEquals(expectedLaptopById.getModel(), resultItem.get().getModel());
//    }

    @Test
    void findOneLaptopByIdTestLaptopIdNotANumber() {
        //Act and Assert
        Assertions.assertThrows(ObjectIdNotANumberException.class,
                () -> this.laptopService.findOneLaptopById("bdbd"));
    }

    @Test
    void findOneComputerByIdTestItemNotFoundException() {
        //Act and Assert
        Assertions.assertThrows(ItemNotFoundException.class,
                () -> this.laptopService.findOneLaptopById(-5 + ""));
    }

//    @Test
//    void findAllLaptopsTestSuccessfull() {
//        //Act
//        List<LaptopViewGeneralModel> expected = this.laptopService.findAllLaptops();
//
//        List<ItemEntity> computers = this.allItemsRepository.findAllItemsByType("laptop");
//
//        Assertions.assertEquals(expected.size(), computers.size());
//    }
}