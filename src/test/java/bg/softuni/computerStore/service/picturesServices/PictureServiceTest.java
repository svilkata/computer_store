package bg.softuni.computerStore.service.picturesServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
class PictureServiceTest {
    @Autowired
    private PictureService pictureService;

    @BeforeEach
    public void setup() {
        this.pictureService.init();
    }


    @Test
    void createPictureEntity() {
    }

    @Test
    void savePhoto() {
    }

    @Test
    void deleteFromPictureRepository() {
    }

    @Test
    void getPictureByPublicId() {
    }
}