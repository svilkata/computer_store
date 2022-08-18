package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.product.AddUpdateComputerBindingDTO;
import bg.softuni.computerStore.model.binding.product.ProductItemTypeBindingDTO;
import bg.softuni.computerStore.model.entity.products.ComputerEntity;
import bg.softuni.computerStore.model.entity.products.ItemEntity;
import bg.softuni.computerStore.model.view.product.ComputerViewGeneralModel;
import bg.softuni.computerStore.repository.products.AllItemsRepository;
import bg.softuni.computerStore.service.ComputerService;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
@WithMockUser(username = "purchase", roles = {"EMPLOYEE_PURCHASES"})
class AddUpdateDeleteComputerControllerTest {
    private final String ADD_NEW_COMPUTER_PREFIX = "/pages/purchases/items/add/computer/";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDetailsService appUserDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private PictureService pictureService;
    @Autowired
    private ComputerService computerService;

    @BeforeEach
    void setUp() {
        this.userService.init();
        this.pictureService.init();
        this.computerService.init();
        loginUser("purchase");
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
    void addNewComputerTestSuccessfull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADD_NEW_COMPUTER_PREFIX + "AK 47 model")
                        .with(csrf()))
                .andExpect(view().name("/purchaseDepartment/addNewItem-computer"))
                .andExpect(model().attributeExists("addComputerBindingDTO"))
                .andExpect(status().isOk());
    }

    @Test
    void addNewComputerConfirmTestSuccessfull() throws Exception {
        //Arrange
        AddUpdateComputerBindingDTO addUpdateComputerBindingDTO = new AddUpdateComputerBindingDTO();
        addUpdateComputerBindingDTO
                .setBrand("Nokia")
                .setModel("5500")
                .setCurrentQuantity(1)
                .setNewQuantityToAdd(12)
                .setBuyingPrice(15 + "")
                .setSellingPrice(16 + "")
                .setVideoCard("Naj-dobrata")
                .setProcessor("Ap 53")
                .setRam("4 GB")
                .setDisk("1 TB");

        mockMvc.perform(MockMvcRequestBuilders.post(ADD_NEW_COMPUTER_PREFIX + "**")
                        .with(csrf())
                        .param("type", "computer")
                        .param("brand", addUpdateComputerBindingDTO.getBrand())
                        .param("model", addUpdateComputerBindingDTO.getModel())
                        .param("currentQuantity", addUpdateComputerBindingDTO.getCurrentQuantity() + "")
                        .param("newQuantityToAdd", addUpdateComputerBindingDTO.getNewQuantityToAdd() + "")
                        .param("buyingPrice", addUpdateComputerBindingDTO.getBuyingPrice())
                        .param("sellingPrice", addUpdateComputerBindingDTO.getSellingPrice())
                        .param("videoCard", addUpdateComputerBindingDTO.getVideoCard())
                        .param("processor", addUpdateComputerBindingDTO.getProcessor())
                        .param("ram", addUpdateComputerBindingDTO.getRam())
                        .param("disk", addUpdateComputerBindingDTO.getDisk())
                )
                .andExpect(status().is3xxRedirection());
//                .andExpect(redirectedUrl())  //I can only hardcore the generated itemId in advance, but no sense
    }

    @Test
    void addNewComputerConfirmTestWrongDataInTheBindingModel() throws Exception {
        //Arrange
        AddUpdateComputerBindingDTO addUpdateComputerBindingDTO = new AddUpdateComputerBindingDTO();
        addUpdateComputerBindingDTO
                .setBrand("Nokia")
                .setModel("5500")
                .setCurrentQuantity(1)
                .setNewQuantityToAdd(12)
                .setBuyingPrice(15 + "")
                .setSellingPrice(16 + "")
                .setVideoCard("Naj-dobrata")
                .setProcessor("Ap 53")
                .setRam("4 GB")
                .setDisk("1 TB");

        mockMvc.perform(MockMvcRequestBuilders.post(ADD_NEW_COMPUTER_PREFIX + "**")
                        .with(csrf())
                        .param("type", "computer")
                        .param("brand", addUpdateComputerBindingDTO.getBrand())
                        .param("model", addUpdateComputerBindingDTO.getModel())
                        .param("currentQuantity", addUpdateComputerBindingDTO.getCurrentQuantity() + "")
                        .param("newQuantityToAdd", addUpdateComputerBindingDTO.getNewQuantityToAdd() + "")
                        .param("buyingPrice", "dewqd")
                        .param("sellingPrice", addUpdateComputerBindingDTO.getSellingPrice())
                        .param("videoCard", "")
                        .param("processor", addUpdateComputerBindingDTO.getProcessor())
                        .param("ram", addUpdateComputerBindingDTO.getRam())
                        .param("disk", addUpdateComputerBindingDTO.getDisk())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("addComputerBindingDTO"))
                .andExpect(redirectedUrl(ADD_NEW_COMPUTER_PREFIX + "**"));  //I can only hardcore the generated itemId in advance, but no sense
    }

    @Test
    void deleteComputerTestSuccessfull() throws Exception {
        String deletedComputerItemID = String.valueOf(5);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/pages/purchases/computers/delete/{id}", deletedComputerItemID)
                        .param("id", deletedComputerItemID)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/all/computer"));
    }

    @Test
    void updateComputerTest() throws Exception {
        String updatedComputerItemId = "1";
        mockMvc.perform(MockMvcRequestBuilders.get("/pages/purchases/computers/{id}/edit", updatedComputerItemId)
                        .with(csrf()))
                .andExpect(view().name("/purchaseDepartment/updateItem-computer"))
                .andExpect(model().attributeExists("editComputerBindingDTO"))
                .andExpect(status().isOk());
    }

    @Test
    void updateComputerConfirmTestSuccessfull() throws Exception {
        //Arrange
        AddUpdateComputerBindingDTO addUpdateComputerBindingDTO = this.computerService.findComputerByIdUpdatingItem(1 + "");
        mockMvc.perform(MockMvcRequestBuilders.patch("/pages/purchases/computers/{id}/edit", String.valueOf(1))
                        .with(csrf())
                        .param("type", "computer")
                        .param("brand", addUpdateComputerBindingDTO.getBrand())
                        .param("model", addUpdateComputerBindingDTO.getModel())
                        .param("currentQuantity", addUpdateComputerBindingDTO.getCurrentQuantity() + "")
                        .param("newQuantityToAdd", addUpdateComputerBindingDTO.getNewQuantityToAdd() + "")
                        .param("buyingPrice", addUpdateComputerBindingDTO.getBuyingPrice())
                        .param("sellingPrice", addUpdateComputerBindingDTO.getSellingPrice())
                        .param("videoCard", addUpdateComputerBindingDTO.getVideoCard())
                        .param("processor", addUpdateComputerBindingDTO.getProcessor())
                        .param("ram", addUpdateComputerBindingDTO.getRam())
                        .param("disk", addUpdateComputerBindingDTO.getDisk())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/items/all/computer/details/" + addUpdateComputerBindingDTO.getItemId()));
    }

    @Test
    void updateComputerConfirmTestWrongDataInTheBindingModel() throws Exception {
        //Arrange
        AddUpdateComputerBindingDTO addUpdateComputerBindingDTO = this.computerService.findComputerByIdUpdatingItem(1 + "");
        mockMvc.perform(MockMvcRequestBuilders.patch("/pages/purchases/computers/{id}/edit", String.valueOf(1))
                        .with(csrf())
                        .param("type", "computer")
                        .param("brand", addUpdateComputerBindingDTO.getBrand())
                        .param("model", addUpdateComputerBindingDTO.getModel())
                        .param("currentQuantity", addUpdateComputerBindingDTO.getCurrentQuantity() + "")
                        .param("newQuantityToAdd", addUpdateComputerBindingDTO.getNewQuantityToAdd() + "")
                        .param("buyingPrice", "-555.34")
                        .param("sellingPrice", addUpdateComputerBindingDTO.getSellingPrice())
                        .param("videoCard", "")
                        .param("processor", addUpdateComputerBindingDTO.getProcessor())
                        .param("ram", addUpdateComputerBindingDTO.getRam())
                        .param("disk", addUpdateComputerBindingDTO.getDisk())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("editComputerBindingDTO"))
                .andExpect(redirectedUrl("/pages/purchases/computers/" + addUpdateComputerBindingDTO.getItemId() + "/edit"));
    }
}