package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.product.ProductItemTypeBindingDTO;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AutoConfigureMockMvc
@WithMockUser(username = "purchase", roles = {"EMPLOYEE_PURCHASES"})
//@WithUserDetails(value = "user1", userDetailsServiceBeanName = "userTestDetailsService")
class AddNewItemControllerTest {
    private final String ADD_NEW_ITEM_PREFIX = "/pages/purchases/items/add";

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
    void addNewItemType() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(ADD_NEW_ITEM_PREFIX).with(csrf()))
                .andExpect(view().name("/purchaseDepartment/addNewItem-choose-type-and-model"))
                .andExpect(status().isOk());
    }

    @Test
    void addNewItemTypeConfirmTestSuccessfullWhenModelExists() throws Exception {
        ProductItemTypeBindingDTO productItemTypeBindingDTO = new ProductItemTypeBindingDTO();
        productItemTypeBindingDTO
                .setItemId(1L)
                .setType("computer")
                .setModel("Dell Vostro 3681 SFF");

        mockMvc.perform(MockMvcRequestBuilders.post(ADD_NEW_ITEM_PREFIX)
                        .with(csrf())
                        .param("type", productItemTypeBindingDTO.getType())
                        .param("model", productItemTypeBindingDTO.getModel())
                        .param("itemId", productItemTypeBindingDTO.getItemId() + "")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ADD_NEW_ITEM_PREFIX))
                .andExpect(flash().attributeExists("productItemTypeBindingDTO"))
                .andExpect(flash().attribute("modelExists", true));
    }

    @Test
    void addNewItemTypeConfirmTestWrongWhenTypeIsMissing() throws Exception {
        ProductItemTypeBindingDTO productItemTypeBindingDTO = new ProductItemTypeBindingDTO();
        productItemTypeBindingDTO
                .setItemId(1L)
                .setType("computer")
                .setModel("Dell Vostro 3681 SFF");

        mockMvc.perform(MockMvcRequestBuilders.post(ADD_NEW_ITEM_PREFIX)
                        .with(csrf())
                        .param("type", "")
                        .param("model", productItemTypeBindingDTO.getModel())
                        .param("itemId", productItemTypeBindingDTO.getItemId() + "")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ADD_NEW_ITEM_PREFIX))
                .andExpect(flash().attributeExists("productItemTypeBindingDTO"));
    }

    @Test
    void addNewItemTypeConfirmTestWrongWhenTypeAndModelBothAreMissing() throws Exception {
        ProductItemTypeBindingDTO productItemTypeBindingDTO = new ProductItemTypeBindingDTO();
        productItemTypeBindingDTO
                .setItemId(1L)
                .setType("computer")
                .setModel("Dell Vostro 3681 SFF");

        mockMvc.perform(MockMvcRequestBuilders.post(ADD_NEW_ITEM_PREFIX)
                        .with(csrf())
                        .param("type", "")
                        .param("model", "")
                        .param("itemId", productItemTypeBindingDTO.getItemId() + "")
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl(ADD_NEW_ITEM_PREFIX))
                .andExpect(flash().attributeExists("productItemTypeBindingDTO"));
    }

    @Test
    void addNewItemTypeConfirmTestSuccessfullWhenModelIsNew() throws Exception {
        ProductItemTypeBindingDTO productItemTypeBindingDTO = new ProductItemTypeBindingDTO();
        productItemTypeBindingDTO
                .setItemId(15L)
                .setType("computer")
                .setModel("My new unique computer model");

        mockMvc.perform(MockMvcRequestBuilders.post(ADD_NEW_ITEM_PREFIX).with(csrf())
                        .param("type", productItemTypeBindingDTO.getType())
                        .param("model", productItemTypeBindingDTO.getModel())
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pages/purchases/items/add/" + productItemTypeBindingDTO.getType() + "/" + productItemTypeBindingDTO.getModel()));
    }
}