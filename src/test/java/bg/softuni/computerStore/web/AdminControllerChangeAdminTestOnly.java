package bg.softuni.computerStore.web;

import bg.softuni.computerStore.repository.users.UserRepository;
import bg.softuni.computerStore.repository.users.UserRoleRepository;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class AdminControllerChangeAdminTestOnly {
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
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void changeAdminUserConfirmTestSuccessful() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(ADMIN_CONTROLLER_PREFIX + "/change-admin-user")
                        .param("username", "purchase")
                        .param("roles", "EMPLOYEE_PURCHASES", "EMPLOYEE_SALES", "ADMIN", "CUSTOMER")
                        .with(csrf()))
                .andExpect(redirectedUrl("/users/logout"));
    }
}