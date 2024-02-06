package bg.softuni.computerStore.service;

import bg.softuni.computerStore.exception.ItemNotFoundException;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.model.binding.product.AddUpdateComputerBindingDTO;
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

import java.util.List;
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
    void findOneComputerByIdTestSuccessfull() {
        //Act
        ComputerViewGeneralModel expectedComputerById = this.computerService.findOneComputerById(1 + "");

        Optional<ItemEntity> resultItem = this.allItemsRepository.findById(1L);

        //Assert
        Assertions.assertEquals(expectedComputerById.getModel(), resultItem.get().getModel());
    }

    @Test
    void findOneComputerByIdTestComputerIdNotANumber() {
        //Act and Assert
        Assertions.assertThrows(ObjectIdNotANumberException.class,
                () -> this.computerService.findOneComputerById("bdbd"));
    }

    @Test
    void findOneComputerByIdTestItemNotFoundException() {
        //Act and Assert
        Assertions.assertThrows(ItemNotFoundException.class,
                () -> this.computerService.findOneComputerById(-5 + ""));
    }

    @Test
    void findAllComputersTestSuccessfull() {
        //Act
        List<ComputerViewGeneralModel> expected = this.computerService.findAllComputers();

        List<ItemEntity> computers = this.allItemsRepository.findAllItemsByType("computer");

        Assertions.assertEquals(expected.size(), computers.size());
    }

    @Test
    void saveNewComputerTestSuccessfull() {
        //Arrange
        AddUpdateComputerBindingDTO addUpdateComputerBindingDTO = new AddUpdateComputerBindingDTO();
        addUpdateComputerBindingDTO
                .setBrand("Nokia")
                .setModel("5500")
                .setCurrentQuantity(1)
                .setNewQuantityToAdd(12)
                .setBuyingPrice(15 + "")
                .setSellingPrice(16 + "")
                .setVideoCard("Naj-dobrata")
                .setProcessor("Ap 53")
                .setRam("4 GB")
                .setDisk("1 TB");

        Long itemId = this.computerService.saveNewComputer(addUpdateComputerBindingDTO);

        //This is the 6th item we generate, and it is computer
        Assertions.assertEquals(8L, itemId);
    }

    @Test
    void deleteComputerAndQuantityTest() {
        //the 5th monitor item we delete
        this.computerService.deleteComputerAndQuantity(5+"");
    }

    @Test
    void findComputerByIdUpdatingItemTestSuccessfull() {
        AddUpdateComputerBindingDTO computerByIdUpdatingItem = this.computerService.findComputerByIdUpdatingItem(1 + "");

        Assertions.assertEquals(1L, computerByIdUpdatingItem.getItemId());
    }

    @Test
    void findComputerByIdUpdatingItemTestItemNotFound() {
        //Act and Assert
        Assertions.assertThrows(ItemNotFoundException.class,
                () -> this.computerService.findComputerByIdUpdatingItem(-5 + ""));
    }

    @Test
    void updateExistingComputerSuccessfull() {
        //Arrange
        AddUpdateComputerBindingDTO addUpdateComputerBindingDTO = new AddUpdateComputerBindingDTO();
        addUpdateComputerBindingDTO
                .setBrand("Lenovo")
                .setModel("Super 85")
                .setCurrentQuantity(1)
                .setNewQuantityToAdd(12)
                .setBuyingPrice(22 + "")
                .setSellingPrice(23.35 + "")
                .setVideoCard("NVidia 5300")
                .setProcessor("eq32re3")
                .setRam("4 GB")
                .setDisk("1 TB")
                .setItemId(1L);

        Long resultItemId = this.computerService.updateExistingComputer(addUpdateComputerBindingDTO);

        Assertions.assertEquals( 1L, resultItemId);

    }
}