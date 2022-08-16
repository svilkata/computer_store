package bg.softuni.computerStore.service.picturesServices;

import bg.softuni.computerStore.model.entity.picture.CloudinaryImage;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@WithMockUser(username = "purchase", roles = {"EMPLOYEE_PURCHASES"})
class CloudinaryServiceTest {
    private static final String MOCK_file = "mock-file";
//    private static final String TEST_URL = "testurl.com";
//    private static final String PUBLIC_ID = "public_id";

    @Mock
    private CloudinaryService cloudinaryService;

    @Test
    void upload() throws IOException {
        //Arrange
        MockMultipartFile multipartFile =
                new MockMultipartFile(MOCK_file,
                        "original file name",
                        MediaType.TEXT_PLAIN_VALUE, "Hello world".getBytes());


        CloudinaryImage uploadedCloduinaryImage = this.cloudinaryService.upload(multipartFile);


    }

    @Test
    void deleteFromCloudinary() {
    }
}