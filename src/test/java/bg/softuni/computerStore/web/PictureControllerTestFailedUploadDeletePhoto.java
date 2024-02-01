package bg.softuni.computerStore.web;

import bg.softuni.computerStore.exception.MyFileDestroyFromCloudinaryException;
import bg.softuni.computerStore.exception.MyFileUploadException;
import bg.softuni.computerStore.model.binding.cloudinary.PictureBindingModel;
import bg.softuni.computerStore.model.entity.picture.PictureEntity;
import bg.softuni.computerStore.service.UserService;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
public class PictureControllerTestFailedUploadDeletePhoto {
    private static final String PURCHASE_CONTROLLER_PREFIX = "/pages/purchases";
    private Long itemId = 1L;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDetailsService appUserDetailsService;
    @Autowired
    private UserService userService;

    private PictureBindingModel pictureBindingModelWrong;
    private MockMultipartFile mockedMultipartFile;

    @MockBean
    private PictureService mockedPictureServiceWrong;

    @BeforeEach
    public void setup() {
        //Arrange
        this.userService.init();
        loginUser("purchase");

        //Arrange more
        //We set the filename to be the name of the field "picture" from PictureBindingModel -
        // so that the reflection to take it from the multipart object correctly. Cool
        mockedMultipartFile = new MockMultipartFile(
                "picture",
                "hello.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Hello World".getBytes());

        pictureBindingModelWrong = new PictureBindingModel().setPicture(mockedMultipartFile);

        PictureEntity pictureEntity = new PictureEntity()
                .setPublicId("public_id")
                .setUrl("url")
                .setItemId(itemId)
                .setId(1L);

        doNothing().when(this.mockedPictureServiceWrong).savePhoto(pictureEntity);
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
    void addComputerPictureTestConflictDuringUpload() throws Exception {
        Exception e = null;
        when(mockedPictureServiceWrong.createPictureEntity(pictureBindingModelWrong.getPicture(), itemId))
                .thenThrow(new MyFileUploadException("Can not upload file", e));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .multipart(PURCHASE_CONTROLLER_PREFIX + "/computers/" + this.itemId + "/addpicture")
                .file(mockedMultipartFile)
                .param("itemId", this.itemId + "")
                .with(csrf());


        mockMvc.perform(builder)
                .andExpect(view().name("errors/upload-to-cloudinary-conflict"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void addComputerPictureTestConflictDuringDelete() throws Exception {
        Exception e = null;
        when(mockedPictureServiceWrong.createPictureEntity(pictureBindingModelWrong.getPicture(), itemId))
                .thenThrow(new MyFileDestroyFromCloudinaryException("Error deleting from cloudinary a file with publicId ", e));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders
                .multipart(PURCHASE_CONTROLLER_PREFIX + "/computers/" + this.itemId + "/addpicture")
                .file(mockedMultipartFile)
                .param("itemId", this.itemId + "")
                .with(csrf());


        mockMvc.perform(builder)
                .andExpect(view().name("errors/delete-from-cloudinary-conflict"))
                .andExpect(status().is4xxClientError());
    }
}