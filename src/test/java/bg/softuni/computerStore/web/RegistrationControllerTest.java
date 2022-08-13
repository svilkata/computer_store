package bg.softuni.computerStore.web;


import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.entity.users.UserRoleEntity;
import bg.softuni.computerStore.model.enums.UserRoleEnum;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;


@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class RegistrationControllerTest {
    private static final String USER_CONTROLLER_PREFIX_REGISTER = "/users/register";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
        this.userService.init();
    }

    @AfterEach
    public void clear() {

    }

    @Test
    void registerTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(USER_CONTROLLER_PREFIX_REGISTER))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/auth-registerUser"));
    }

    @Test
    void registerConfirmTestSuccessfull() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_PREFIX_REGISTER)
                        .param("username", "pesh")
                        .param("email", "Pesho@mail.bg")
                        .param("firstName", "Pesho")
                        .param("lastName", "Peshovich")
                        .param("password", "123456")
                        .param("confirmPassword", "123456")
                        .with(csrf())).
                andExpect(status().is3xxRedirection());
    }

    @Test
    void registerConfirmWithDifferentConfirmPassword() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_PREFIX_REGISTER)
                        .param("username", "Todor")
                        .param("password", "1234")
                        .param("confirmPassword", "1111")
                        .with(csrf())).
                andExpect(status().is3xxRedirection());
    }

    @Test
    void registerConfirmWithWrongBindingModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_PREFIX_REGISTER)
                        .param("username", "Gs")
                        .param("password", "1")
                        .param("confirmPassword", "1")
                        .with(csrf())).
                andExpect(status().is3xxRedirection());
    }

    @Test
    void registerConfirmWithExistUserName() throws Exception {
        UserEntity user = new UserEntity();
        user
                .setUsername("vankata")
                .setFirstName("Ivan")
                .setLastName("Ivanov")
                .setEmail("vanka@tisipi4.com");

        user.setPassword(passwordEncoder.encode("1111"));

        Optional<UserRoleEntity> userRoleOpt = userRoleRepository.findByUserRole((UserRoleEnum.CUSTOMER));
        user.setUserRoles(Set.of(userRoleOpt.get()));
        UserEntity savedUser = userRepository.save(user);

        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_PREFIX_REGISTER)
                        .param("username", "vankata")
                        .param("password", "111")
                        .param("confirmPassword", "111")
                        .with(csrf())).
                andExpect(status().is3xxRedirection());
    }
}