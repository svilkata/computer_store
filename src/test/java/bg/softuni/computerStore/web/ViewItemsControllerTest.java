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
class ViewItemsControllerTest {
    private final String COMPUTER_PREFIX = "/items/all/computer";
    private final String MONITOR_PREFIX = "/items/all/monitor";
    private final String LAPTOP_PREFIX = "/items/all/laptop";

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
        computerService.init();
        monitorService.init();
        laptopService.init();
    }

    @Test
    void viewOneComputerTestSuccessfull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(COMPUTER_PREFIX + "/details/" + 1))
                .andExpect(view().name("/viewItems/one-computer-details"))
                .andExpect(model().attributeExists("oneComputer"))
                .andExpect(status().isOk());
    }

    @Test
    void viewOneComputerTestNotExistingItemId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(COMPUTER_PREFIX + "/details/" + -5))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void viewOneComputerTestItemIdNotANumber() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(COMPUTER_PREFIX + "/details/" + "deqwd"))
                .andExpect(view().name("errors/item-not-found"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void viewAllComputersTestSuccessfull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(COMPUTER_PREFIX))
                .andExpect(view().name("/viewItems/all-computers"))
                .andExpect(model().attributeExists("computers"))
                .andExpect(status().isOk());
    }

    @Test
    void viewOneMonitorTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(MONITOR_PREFIX + "/details/" + 8))
                .andExpect(view().name("/viewItems/one-monitor-details"))
                .andExpect(model().attributeExists("oneMonitor"))
                .andExpect(status().isOk());
    }

    @Test
    void viewAllMonitorsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(MONITOR_PREFIX))
                .andExpect(view().name("/viewItems/all-monitors"))
                .andExpect(model().attributeExists("monitors"))
                .andExpect(status().isOk());
    }

    @Test
    void viewOneLaptopTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(LAPTOP_PREFIX + "/details/" + 9))
                .andExpect(view().name("/viewItems/one-laptop-details"))
                .andExpect(model().attributeExists("oneLaptop"))
                .andExpect(status().isOk());
    }

    @Test
    void viewAllLaptopsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(LAPTOP_PREFIX))
                .andExpect(view().name("/viewItems/all-laptops"))
                .andExpect(model().attributeExists("laptops"))
                .andExpect(status().isOk());
    }


}