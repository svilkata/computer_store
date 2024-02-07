package bg.softuni.computerStore.service;

import bg.softuni.computerStore.exception.ItemsWithTypeNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
class MonitorServiceTestEmptyMonitorList {
    @Autowired
    private MonitorService monitorService;

    @BeforeEach
    void init() {
    }

    @Test
    void findAllComputersTestEmptySize() {
        //Act and Assert
        Assertions.assertThrows(ItemsWithTypeNotFoundException.class,
                () -> this.monitorService.findAllMonitors());
    }

}