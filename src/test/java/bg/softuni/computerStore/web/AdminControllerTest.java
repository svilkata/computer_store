package bg.softuni.computerStore.web;

import bg.softuni.computerStore.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
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
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class AdminControllerTest {
    private static final String ADMIN_CONTROLLER_PREFIX = "/pages/admins";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserDetailsService appUserDetailsService;
    @Autowired
    private UserService userService;

    @BeforeEach
    public void setup() {
        this.userService.init();
        loginUser("admin");
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

    @AfterEach
    void clear() {

    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addEmployeeRoleTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                        ADMIN_CONTROLLER_PREFIX + "/set-user-role"))
                .andExpect(view().name("/user/add-or-edit-user-role"))
                .andExpect(model().attributeExists("userRolesBindingDTO"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addUserRoleConfirmTestSuccessfull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CONTROLLER_PREFIX + "/set-user-role")
                        .param("username", "purchase")
                        .param("roles", "EMPLOYEE_PURCHASES", "EMPLOYEE_SALES")
                        .with(csrf()))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addUserRoleConfirmTestWhenOnlyOneRoleSelected() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CONTROLLER_PREFIX + "/set-user-role")
                        .param("username", "purchase")
                        .param("roles", "EMPLOYEE_SALES")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pages/admins/set-user-role"))
                .andExpect(flash().attribute("atLeastTwoRolesShouldBeSelected", true));
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addUserRoleConfirmTestWhenAdminRoleIsPresent() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CONTROLLER_PREFIX + "/set-user-role")
                        .param("username", "purchase")
                        .param("roles", "ADMIN", "EMPLOYEE_PURCHASES")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pages/admins/set-user-role"))
                .andExpect(flash().attribute("oneAdminOnlyPossible", true));
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addUserRoleConfirmTestWhenEmployeeNotSelected() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CONTROLLER_PREFIX + "/set-user-role")
                        .param("username", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pages/admins/set-user-role"))
                .andExpect(flash().attribute("employeeNotSelected", true));
    }


    @Test
    @Order(6)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void statisticsHttpRequestsTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                                ADMIN_CONTROLLER_PREFIX + "/statshttprequests")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/stats/stats-httprequests"))
                .andExpect(model().attributeExists("stats"));

    }

    @Test
    @Order(7)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void statisticsSalesTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                                ADMIN_CONTROLLER_PREFIX + "/statssales")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/stats/stats-sales"))
                .andExpect(model().attributeExists("stats"));
    }

    @Test
    @Order(8)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void changeAdminUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                                ADMIN_CONTROLLER_PREFIX + "/change-admin-user")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/change-admin-user-role"))
                .andExpect(model().attributeExists("employees"))
                .andExpect(model().attributeExists("userRolesBindingDTO"));
    }


    @Test
    @Order(9)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void changeAdminUserConfirmTestWhenEmployeeNotSelected() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CONTROLLER_PREFIX + "/change-admin-user")
                        .param("username", "")
                        .param("roles", "")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pages/admins/change-admin-user"))
                .andExpect(flash().attribute("employeeNotSelected", true));
    }

    @Test
    @Order(10)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void changeAdminUserConfirmTestWhenLessThanFourRoles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CONTROLLER_PREFIX + "/change-admin-user")
                        .param("username", "sales")
                        .param("roles", "EMPLOYEE_PURCHASES", "ADMIN")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pages/admins/change-admin-user"))
                .andExpect(flash().attribute("adminChanging", true));
    }


    @Test
    @Order(11)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void registerNewEmployeeTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                        ADMIN_CONTROLLER_PREFIX + "/register-new-employee"))
                .andExpect(view().name("/user/registerNewEmployee"))
                .andExpect(model().attributeExists("employeeRegistrationModel"))
                .andExpect(status().isOk());
    }

    @Test
    @Order(12)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void registerNewEmployeeConfirmTestSuccessfull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CONTROLLER_PREFIX + "/register-new-employee")
                        .param("username", "Tisho")
                        .param("email", "xadqw@dqd.com")
                        .param("firstName", "Tihomir")
                        .param("lastName", "Tihomirov")
                        .param("password", "1234")
                        .param("roles", "EMPLOYEE_PURCHASES", "CUSTOMER")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @Order(13)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void registerNewEmployeeConfirmTestCreatingEmployeeHasErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CONTROLLER_PREFIX + "/register-new-employee")
                        .param("username", "Tisho")
                        .param("email", "")
                        .param("firstName", "")
                        .param("lastName", "Tihomirov")
                        .param("password", "1234")
                        .param("roles", "EMPLOYEE_PURCHASES", "CUSTOMER")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pages/admins/register-new-employee"));
    }

    @Test
    @Order(14)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void registerNewEmployeeConfirmTestLessThanTwoRoles() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CONTROLLER_PREFIX + "/register-new-employee")
                        .param("username", "Tisho")
                        .param("email", "Tifwf@dqwed.com")
                        .param("firstName", "Tihomir")
                        .param("lastName", "Tihomirov")
                        .param("password", "1234")
                        .param("roles", "EMPLOYEE_PURCHASES")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("atLeastTwoRolesShouldBeSelected", true))
                .andExpect(redirectedUrl("/pages/admins/register-new-employee"));
    }

    @Test
    @Order(15)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void registerNewEmployeeConfirmTestLessThanTwoRolesAndBindingModelHasErrors() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CONTROLLER_PREFIX + "/register-new-employee")
                        .param("username", "")
                        .param("email", "Tifwf@dqwed.com")
                        .param("firstName", "")
                        .param("lastName", "Tihomirov")
                        .param("password", "1234")
                        .param("roles", "EMPLOYEE_PURCHASES")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("atLeastTwoRolesShouldBeSelected", true))
                .andExpect(redirectedUrl("/pages/admins/register-new-employee"));
    }
}