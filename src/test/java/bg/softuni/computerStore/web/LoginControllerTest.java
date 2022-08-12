package bg.softuni.computerStore.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
class LoginControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String USER_CONTROLLER_PREFIX = "/users";

    @Test
    void login() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(
                        USER_CONTROLLER_PREFIX + "/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/auth-login"));
    }

    @Test
    void failedLogin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post(USER_CONTROLLER_PREFIX + "/login-error")
                        .param("name", "Starshi")
                        .param("password", "11111")
                        .with(csrf())).
                andExpect(status().is3xxRedirection());
    }
}



