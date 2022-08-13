package bg.softuni.computerStore.service;

import bg.softuni.computerStore.service.picturesServices.CloudinaryAndPictureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
class CloudinaryAndPictureServiceTest {
    @Autowired
    private CloudinaryAndPictureService cloudinaryAndPictureService;

    @BeforeEach
    public void setup() {

    }

    @Test
    void viewOneComputerTest() throws Exception {
        Long resultCountOfInits = this.cloudinaryAndPictureService.init();
        Long expectdCountShouldBe8 = 8L;

        Assertions.assertEquals(expectdCountShouldBe8, resultCountOfInits);
    }

}