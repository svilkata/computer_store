package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.cloudinary.PictureBindingModel;
import bg.softuni.computerStore.model.entity.picture.PictureEntity;
import bg.softuni.computerStore.service.UserService;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WithMockUser(username = "purchase", roles = {"EMPLOYEE_PURCHASES"})
public class PictureControllerTest {
    private static final String PURCHASE_CONTROLLER_PREFIX = "/pages/purchases";
    private Long itemId = 1L;

    @Autowired
    private PictureController pictureController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDetailsService appUserDetailsService;
    @Autowired
    private UserService userService;
    private PictureBindingModel pictureBindingModel;
    private MockMultipartFile mockedMultipartFile;

    @Mock
    private PictureService mockedPictureService;

    @BeforeEach
    public void setup() {
        //Arrange
        this.userService.init();
        loginUser("purchase");

        //Arrange more
        mockedMultipartFile = new MockMultipartFile(
                "file",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello World".getBytes());

        pictureBindingModel = new PictureBindingModel().setPicture(mockedMultipartFile);

        PictureEntity pictureEntity = new PictureEntity()
                .setPublicId("public_id")
                .setUrl("url")
                .setItemId(itemId)
                .setId(1L);

        doReturn(pictureEntity).when(mockedPictureService)
                .createPictureEntity(pictureBindingModel.getPicture(), itemId);
//        when(this.mockedPictureService.createPictureEntity(pictureBindingModel.getPicture(), itemId))
//                .thenReturn(pictureEntity);

        doNothing().when(this.mockedPictureService).savePhoto(pictureEntity);

        pictureController = new PictureController(mockedPictureService);
    }

    private void loginUser(String username) {
        //The login process of user with username "admin"  doing it below
        UserDetails userDetails =
                appUserDetailsService.loadUserByUsername(username);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.
                getContext().
                setAuthentication(authentication);
    }

    @Test
    void addComputerPictureTest() throws Exception {
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
//                .multipart()
//                .file(mockedMultipartFile)
                .post(PURCHASE_CONTROLLER_PREFIX + "/computers/" + this.itemId + "/addpicture")
                .param("itemId", this.itemId + "")
                .with(csrf());



        mockMvc.perform(builder)
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/all/computer/details/" + this.itemId));
    }

//    @Test
//    void addMonitorPicture() {
//    }
}