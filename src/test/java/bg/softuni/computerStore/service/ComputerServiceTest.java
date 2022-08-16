package bg.softuni.computerStore.service;

import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@AutoConfigureTestDatabase
class ComputerServiceTest {
    @Autowired
    private PictureService pictureService;
    @Autowired
    private ComputerService computerService;
    @Autowired
    private AllItemsRepository allItemsRepository;

    @BeforeEach
    void init() {
        //Arrange
        this.pictureService.init();
        this.computerService.init();
    }


    @Test
    void findOneComputerByIdTest() {
        //Act
        ComputerViewGeneralModel expectedComputerById = this.computerService.findOneComputerById(1 + "");

        Optional<ItemEntity> resultItem = this.allItemsRepository.findById(1L);

        //Assert
        Assertions.assertEquals(expectedComputerById.getModel(), resultItem.get().getModel());
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