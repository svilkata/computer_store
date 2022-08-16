package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.entity.picture.PictureEntity;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WithMockUser(username = "purchase", roles = {"EMPLOYEE_PURCHASES"})
class PictureControllerTest {
    private static final String MOCK_file = "mock-file";

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PictureEntity mockedPictureEntity;

    @Test
    void addComputerPictureTest() {
        //Arrange
        MockMultipartFile multipartFile =
                new MockMultipartFile(MOCK_file,
                        "original file name",
                        MediaType.TEXT_PLAIN_VALUE, "Hello world".getBytes());



    }

//    @Test
//    void addMonitorPicture() {
//    }
}