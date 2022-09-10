package bg.softuni.computerStore.service.oauth2;

import bg.softuni.computerStore.repository.users.UserRepository;
import bg.softuni.computerStore.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class OAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final UserService userService;
    private final UserRepository userRepository;

    public OAuthSuccessHandler(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        setDefaultTargetUrl("/");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        if (authentication instanceof OAuth2AuthenticationToken oAuth2AuthenticationToken) {
            OAuth2User principal = oAuth2AuthenticationToken.getPrincipal();
            var username = principal.getAttribute("name").toString();
            var email = principal.getAttribute("email").toString();

            //if true, then either the email or the username exists
            String customerFromFacebookIfNotExist = this.userService.createCustomerFromFacebookIfNotExist(username, email);
            if (customerFromFacebookIfNotExist.equals("username")) {
                //Login with correct username only  TODO: it is not completely ok like that
                this.userService.login(username);
            } else if (customerFromFacebookIfNotExist.equals("email")) {
                //Login with correct email only    TODO: it is not completely ok like that
                this.userService.login(this.userRepository.findByEmail(email).get().getUsername());
            } else if (customerFromFacebookIfNotExist.equals("saved new customer in the database") ||
                    customerFromFacebookIfNotExist.equals("user exists in the database")) {
                //THE LOGIN PROCESS
                this.userService.login(username);
            }
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
