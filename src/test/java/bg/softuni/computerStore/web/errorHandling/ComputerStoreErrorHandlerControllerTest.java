package bg.softuni.computerStore.web.errorHandling;

import bg.softuni.computerStore.service.UserService;
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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
//@ExtendWith(MockitoExtension.class)
class ComputerStoreErrorHandlerControllerTest {
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

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void handleErrorTestDefault() throws Exception {
        MockHttpServletRequestBuilder mockedBuilder =
                MockMvcRequestBuilders.get("/error");

        mockMvc.perform(mockedBuilder)
                .andExpect(view().name("errors/default-error"))
                .andExpect(status().isOk());  //ok status for going to the errors/default-error  page
    }

//    @Test
//    @WithMockUser(username = "admin", roles = {"ADMIN"})
//    void handleErrorTest() throws Exception {
//        MockHttpServletRequestBuilder mockedBuilder =
//                MockMvcRequestBuilders.get("/error");
//
//        //how to set a result
//        @GetMapping
//        private ResponseEntity returnResponse(){
//            return ResponseEntity.builder.status()notFound().build();
//        }
//
//        mockMvc.perform(mockedBuilder)
//                .andExpect(view().name("errors/error-404"))
//                .andExpect(status().isOk()); //ok status for going to the errors/error-404 page
//    }

}