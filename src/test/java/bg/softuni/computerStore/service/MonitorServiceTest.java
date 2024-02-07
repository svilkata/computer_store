package bg.softuni.computerStore.service;

import bg.softuni.computerStore.exception.ItemNotFoundException;
import bg.softuni.computerStore.exception.ObjectIdNotANumberException;
import bg.softuni.computerStore.model.binding.product.AddUpdateMonitorBindingDTO;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.view.product.MonitorViewGeneralModel;
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
class MonitorServiceTest {
    @Autowired
    private PictureService pictureService;
    @Autowired
    private MonitorService monitorService;
    @Autowired
    private AllItemsRepository allItemsRepository;

    @BeforeEach
    void init() {
        //Arrange
        this.pictureService.init();
        this.monitorService.init();
    }

    @Test
    void findOneMonitorByIdTestSuccessfull() {
        //Act
        MonitorViewGeneralModel expectedMonitorById = this.monitorService.findOneMonitorById(1 + "");

        Optional<ItemEntity> resultItem = this.allItemsRepository.findById(1L);

        //Assert
        Assertions.assertEquals(expectedMonitorById.getModel(), resultItem.get().getModel());
    }

    @Test
    void findOneMonitorByIdTestComputerIdNotANumber() {
        //Act and Assert
        Assertions.assertThrows(ObjectIdNotANumberException.class,
                () -> this.monitorService.findOneMonitorById("bdbd"));
    }

    @Test
    void findOneMonitorByIdTestItemNotFoundException() {
        //Act and Assert
        Assertions.assertThrows(ItemNotFoundException.class,
                () -> this.monitorService.findOneMonitorById(-5 + ""));
    }

    @Test
    void findAllMonitorsTestSuccessfull() {
        //Act
        List<MonitorViewGeneralModel> expected = this.monitorService.findAllMonitors();

        List<ItemEntity> monitors = this.allItemsRepository.findAllItemsByType("monitor");

        Assertions.assertEquals(expected.size(), monitors.size());
    }

    @Test
    void saveNewMonitorTestSuccessfull() {
        //Arrange
        AddUpdateMonitorBindingDTO addUpdateMonitorBindingDTO = new AddUpdateMonitorBindingDTO();
        addUpdateMonitorBindingDTO
                .setBrand("Nokia")
                .setModel("5500")
                .setCurrentQuantity(1)
                .setNewQuantityToAdd(12)
                .setBuyingPrice(15 + "")
                .setSellingPrice(16 + "")
                .setSize("21inches")
                .setResolution("2048/1768")
                .setMatrixType("best one")
                .setViewAngle("150 degress")
                .setRefreshRate("100Hz");


        Long itemId = this.monitorService.saveNewMonitor(addUpdateMonitorBindingDTO);

        //This is the 4th item we generate, and it is monitor
        Assertions.assertEquals(4L, itemId);
    }

    @Test
    void deleteComputerAndQuantityTest() {
        //the 3d monitor item we delete
        this.monitorService.deleteMonitorAndQuantity(3 +"");
    }

    @Test
    void findMonitorByIdUpdatingItemTestSuccessfull() {
        AddUpdateMonitorBindingDTO monitorByIdUpdatingItem = this.monitorService.findMonitorByIdUpdatingItem(1 + "");

        Assertions.assertEquals(1L, monitorByIdUpdatingItem.getItemId());
    }

    @Test
    void findMonitorByIdUpdatingItemTestItemNotFound() {
        //Act and Assert
        Assertions.assertThrows(ItemNotFoundException.class,
                () -> this.monitorService.findMonitorByIdUpdatingItem(-5 + ""));
    }

    @Test
    void updateExistingComputerSuccessfull() {
        //Arrange
        AddUpdateMonitorBindingDTO addUpdateMonitorBindingDTO = new AddUpdateMonitorBindingDTO();
        addUpdateMonitorBindingDTO
                .setBrand("Siemens")
                .setModel("verfe")
                .setCurrentQuantity(1)
                .setNewQuantityToAdd(12)
                .setBuyingPrice(15 + "")
                .setSellingPrice(16 + "")
                .setSize("21inches")
                .setResolution("2048/1768")
                .setMatrixType("best one")
                .setViewAngle("150 degress")
                .setRefreshRate("100Hz")
                .setItemId(1L);

        Long resultItemId = this.monitorService.updateExistingMonitor(addUpdateMonitorBindingDTO);

        Assertions.assertEquals( 1L, resultItemId);
    }
}