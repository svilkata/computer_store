package bg.softuni.computerStore.web;

import bg.softuni.computerStore.service.ComputerService;
import bg.softuni.computerStore.service.LaptopService;
import bg.softuni.computerStore.service.MonitorService;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WithAnonymousUser
class ViewItemsControllerTestWhenNoItemsPresent {
    private final String COMPUTER_PREFIX = "/items/all/computer";

    @Autowired
    private ComputerService computerService;
    @Autowired
    private MonitorService monitorService;
    @Autowired
    private LaptopService laptopService;
    @Autowired
    private PictureService pictureService;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        pictureService.init();
    }

    @Test
    void viewAllComputersTestWhenNoItemsPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(COMPUTER_PREFIX))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }
}