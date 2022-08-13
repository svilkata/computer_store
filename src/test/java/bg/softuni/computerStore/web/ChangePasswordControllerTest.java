package bg.softuni.computerStore.web;

import bg.softuni.computerStore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
//@AutoConfigureTestDatabase
class ChangePasswordControllerTest {
    private static final String SALES_CONTROLLER_CHANGEPASSWORD = "/users/changepassword";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserDetailsService appUserDetailsService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        this.userService.init();
        loginUser("sales");
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
    @WithMockUser(username = "sales", roles = {"EMPLOYEE_SALES"})
    public void changePasswordTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(SALES_CONTROLLER_CHANGEPASSWORD))
                .andExpect(view().name("/user/auth-changePassword"))
                .andExpect(model().attributeExists("changeUserPasswordDTO"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "sales", roles = {"EMPLOYEE_SALES"})
    public void registerTestSuccessfull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(SALES_CONTROLLER_CHANGEPASSWORD)
                        .param("currentPassword", "11111")
                        .param("newPassword", "22222")
                        .param("confirmNewPassword", "22222")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/logout"));
    }

    @Test
    @WithMockUser(username = "sales", roles = {"EMPLOYEE_SALES"})
    public void registerTestWhenUserEnteredWrongCurrentPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(SALES_CONTROLLER_CHANGEPASSWORD)
                        .param("currentPassword", "dqwdw")
                        .param("newPassword", "22222")
                        .param("confirmNewPassword", "11")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("changeUserPasswordDTO"))
                .andExpect(flash().attribute("userEnteredWrongCurrentPass", true))
                .andExpect(redirectedUrl("/users/changepassword"));
    }

    @Test
    @WithMockUser(username = "sales", roles = {"EMPLOYEE_SALES"})
    public void registerTestWhenOnlyBindingErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(SALES_CONTROLLER_CHANGEPASSWORD)
                        .param("currentPassword", "11111")
                        .param("newPassword", "22222")
                        .param("confirmNewPassword", "11")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attributeExists("changeUserPasswordDTO"))
                .andExpect(redirectedUrl("/users/changepassword"));
    }
}