package bg.softuni.computerStore.web;

import bg.softuni.computerStore.model.binding.user.UserRegisterBindingDTO;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.repository.users.UserRepository;
import bg.softuni.computerStore.repository.users.UserRoleRepository;
import bg.softuni.computerStore.service.UserService;
import org.junit.jupiter.api.AfterEach;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup() {
        this.userService.init();
    }

    @AfterEach
    public void clear() {
        userRepository.deleteAll();
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void addEmployeeRoleTest() throws Exception {
        //The login process
        UserDetails userDetails =
                appUserDetailsService.loadUserByUsername("admin");

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(
                        userDetails,
                        userDetails.getPassword(),
                        userDetails.getAuthorities()
                );

        SecurityContextHolder.
                getContext().
                setAuthentication(authentication);

        mockMvc.perform(MockMvcRequestBuilders.get(
                                ADMIN_CONTROLLER_PREFIX + "/set-user-role")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/add-or-edit-user-role"))
                .andExpect(model().attributeExists("userRolesBindingDTO"));
    }

//    @Test
//    void statisticsHttpRequests() {
//    }
//
//    @Test
//    void statisticsSales() {
//    }
//
//    @Test
//    void addUserRoleConfirm() {
//    }
//
//    @Test
//    void changeAdminUser() {
//    }
//
//    @Test
//    void changeAdminUserConfirm() {
//    }
//
//    @Test
//    void registerNewEmployee() {
//    }
//
//    @Test
//    void registerNewEmployeeConfirm() {
//    }
}