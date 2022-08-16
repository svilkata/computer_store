package bg.softuni.computerStore.service;

import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@AutoConfigureTestDatabase
class PictureServiceTest {
    @Autowired
    private PictureService pictureService;

    @BeforeEach
    public void setup() {

    }

    @Test
    void viewOneComputerTest() throws Exception {
        Long resultCountOfInits = this.pictureService.init();
        Long expectdCountShouldBe8 = 8L;

        Assertions.assertEquals(expectdCountShouldBe8, resultCountOfInits);
    }

}