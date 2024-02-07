package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.product.AddUpdateMonitorBindingDTO;
import bg.softuni.computerStore.service.MonitorService;
import bg.softuni.computerStore.service.UserService;
import bg.softuni.computerStore.service.picturesServices.PictureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithMockUser(username = "purchase", roles = {"EMPLOYEE_PURCHASES"})
class AddUpdateDeleteMonitorControllerTest {
    private final String ADD_NEW_MONITOR_PREFIX = "/pages/purchases/items/add/monitor/";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDetailsService appUserDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private MonitorService monitorService;

    @BeforeEach
    void setUp() {
        this.userService.init();
        this.pictureService.init();
        this.monitorService.init();
        loginUser("purchase");
    }

    private void loginUser(String username) {
        //The login process of user with username "admin"  doing it below
        UserDetails userDetails = appUserDetailsService.loadUserByUsername(username);

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
    void addNewMonitorTestSuccessfull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADD_NEW_MONITOR_PREFIX + "AK 47 model")
                        .with(csrf()))
                .andExpect(view().name("/purchaseDepartment/addNewItem-monitor"))
                .andExpect(model().attributeExists("addMonitorBindingDTO"))
                .andExpect(status().isOk());
    }

    @Test
    void addNewMonitorConfirmTestSuccessfull() throws Exception {
        //Arrange
        AddUpdateMonitorBindingDTO addUpdateMonitorBindingDTO = new AddUpdateMonitorBindingDTO();
        addUpdateMonitorBindingDTO
                .setBrand("Acer")
                .setModel("GGG121")
                .setCurrentQuantity(1)
                .setNewQuantityToAdd(12)
                .setBuyingPrice(15 + "")
                .setSellingPrice(16 + "")
                .setSize("21inches")
                .setResolution("2048/1768")
                .setMatrixType("worst one")
                .setViewAngle("170 degress")
                .setRefreshRate("60Hz");

        mockMvc.perform(MockMvcRequestBuilders.post(ADD_NEW_MONITOR_PREFIX + "**").with(csrf())
                        .param("type", "monitor")
                        .param("brand", addUpdateMonitorBindingDTO.getBrand())
                        .param("model", addUpdateMonitorBindingDTO.getModel())
                        .param("currentQuantity", addUpdateMonitorBindingDTO.getCurrentQuantity() + "")
                        .param("newQuantityToAdd", addUpdateMonitorBindingDTO.getNewQuantityToAdd() + "")
                        .param("buyingPrice", addUpdateMonitorBindingDTO.getBuyingPrice())
                        .param("sellingPrice", addUpdateMonitorBindingDTO.getSellingPrice())
                        .param("size", addUpdateMonitorBindingDTO.getSize())
                        .param("resolution", addUpdateMonitorBindingDTO.getResolution())
                        .param("matrixType", addUpdateMonitorBindingDTO.getMatrixType())
                        .param("viewAngle", addUpdateMonitorBindingDTO.getViewAngle())
                        .param("refreshRate", addUpdateMonitorBindingDTO.getRefreshRate())
                )
                .andExpect(status().is3xxRedirection());
//                .andExpect(redirectedUrl())  //I can only hardcore the generated itemId in advance, but no sense
    }

    @Test
    void addNewComputerConfirmTestWrongDataInTheBindingModel() throws Exception {
        //Arrange
        AddUpdateMonitorBindingDTO addUpdateMonitorBindingDTO = new AddUpdateMonitorBindingDTO();
        addUpdateMonitorBindingDTO
                .setBrand("Acer")
                .setModel("GGG121")
                .setCurrentQuantity(1)
                .setNewQuantityToAdd(12)
                .setBuyingPrice(15 + "")
                .setSellingPrice(16 + "")
                .setSize("21inches")
                .setResolution("2048/1768")
                .setMatrixType("worst one")
                .setViewAngle("170 degress")
                .setRefreshRate("60Hz");

        mockMvc.perform(MockMvcRequestBuilders.post(ADD_NEW_MONITOR_PREFIX + "**")
                        .with(csrf())
                        .param("type", "monitor")
                        .param("brand", addUpdateMonitorBindingDTO.getBrand())
                        .param("model", addUpdateMonitorBindingDTO.getModel())
                        .param("currentQuantity", addUpdateMonitorBindingDTO.getCurrentQuantity() + "")
                        .param("newQuantityToAdd", addUpdateMonitorBindingDTO.getNewQuantityToAdd() + "")
                        .param("buyingPrice", addUpdateMonitorBindingDTO.getBuyingPrice())
                        .param("sellingPrice", "cadd")
                        .param("size", addUpdateMonitorBindingDTO.getSize())
                        .param("resolution", "")
                        .param("matrixType", addUpdateMonitorBindingDTO.getMatrixType())
                        .param("viewAngle", addUpdateMonitorBindingDTO.getViewAngle())
                        .param("refreshRate", addUpdateMonitorBindingDTO.getRefreshRate())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("addMonitorBindingDTO"))
                .andExpect(redirectedUrl(ADD_NEW_MONITOR_PREFIX + "**"));  //I can only hardcore the generated itemId in advance, but no sense
    }


    @Test
    void deleteMonitorTestSuccessfull() throws Exception {
        String deletedMonitorItemID = String.valueOf(3);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/pages/purchases/monitors/delete/{id}", deletedMonitorItemID)
                        .param("id", deletedMonitorItemID)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/all/monitor"));
    }

    @Test
    void updateMonitorTest() throws Exception {
        String updatedMonitorItemId = "1";
        mockMvc.perform(MockMvcRequestBuilders.get("/pages/purchases/monitors/{id}/edit", updatedMonitorItemId)
                        .with(csrf()))
                .andExpect(view().name("/purchaseDepartment/updateItem-monitor"))
                .andExpect(model().attributeExists("editMonitorBindingDTO"))
                .andExpect(status().isOk());
    }


    @Test
    void updateComputerConfirmTestSuccessfull() throws Exception {
        //Arrange
        String updatedMonitorItemId = "1";
        AddUpdateMonitorBindingDTO addUpdateMonitorBindingDTO = this.monitorService.findMonitorByIdUpdatingItem(updatedMonitorItemId);
        mockMvc.perform(MockMvcRequestBuilders.patch("/pages/purchases/monitors/{id}/edit", updatedMonitorItemId)
                        .with(csrf())
                        .param("type", "monitor")
                        .param("brand", addUpdateMonitorBindingDTO.getBrand())
                        .param("model", addUpdateMonitorBindingDTO.getModel())
                        .param("currentQuantity", addUpdateMonitorBindingDTO.getCurrentQuantity() + "")
                        .param("newQuantityToAdd", addUpdateMonitorBindingDTO.getNewQuantityToAdd() + "")
                        .param("buyingPrice", addUpdateMonitorBindingDTO.getBuyingPrice())
                        .param("sellingPrice", addUpdateMonitorBindingDTO.getSellingPrice())
                        .param("size", addUpdateMonitorBindingDTO.getSize())
                        .param("resolution", addUpdateMonitorBindingDTO.getResolution())
                        .param("matrixType", addUpdateMonitorBindingDTO.getMatrixType())
                        .param("viewAngle", addUpdateMonitorBindingDTO.getViewAngle())
                        .param("refreshRate", addUpdateMonitorBindingDTO.getRefreshRate())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/all/monitor/details/" + addUpdateMonitorBindingDTO.getItemId()));
    }

    @Test
    void updateComputerConfirmTestWrongDataInTheBindingModel() throws Exception {
        //Arrange
        String updatedMonitorItemId = "1";
        AddUpdateMonitorBindingDTO addUpdateMonitorBindingDTO = this.monitorService.findMonitorByIdUpdatingItem(updatedMonitorItemId);
        mockMvc.perform(MockMvcRequestBuilders.patch("/pages/purchases/monitors/{id}/edit", updatedMonitorItemId)
                        .with(csrf())
                        .param("type", "monitor")
                        .param("brand", addUpdateMonitorBindingDTO.getBrand())
                        .param("model", addUpdateMonitorBindingDTO.getModel())
                        .param("currentQuantity", addUpdateMonitorBindingDTO.getCurrentQuantity() + "")
                        .param("newQuantityToAdd", addUpdateMonitorBindingDTO.getNewQuantityToAdd() + "")
                        .param("buyingPrice", addUpdateMonitorBindingDTO.getBuyingPrice())
                        .param("sellingPrice", "cadd")
                        .param("size", addUpdateMonitorBindingDTO.getSize())
                        .param("resolution", "")
                        .param("matrixType", addUpdateMonitorBindingDTO.getMatrixType())
                        .param("viewAngle", addUpdateMonitorBindingDTO.getViewAngle())
                        .param("refreshRate", addUpdateMonitorBindingDTO.getRefreshRate())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("editMonitorBindingDTO"))
                .andExpect(redirectedUrl("/pages/purchases/monitors/" + addUpdateMonitorBindingDTO.getItemId() + "/edit"));
    }
}