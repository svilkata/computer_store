package bg.softuni.computerStore.config;

import bg.softuni.computerStore.model.enums.UserRoleEnum;
import bg.softuni.computerStore.repository.users.UserRepository;
import bg.softuni.computerStore.service.AppUserDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
//@EnableGlobalMethodSecurity(prePostEnabled = true) - we will not use @PreAuthorize on method level
//@EnableWebSecurity - we will not use @PreAuthorize on method level
public class SecurityConfig {
    //Here we have to expose 3 things:
    // 1. PasswordEncoder
    // 2. SecurityFilterChain
    // 3. UserDetailsService

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.
                // define which requests are allowed and which not
                        authorizeRequests().
                // everyone can download static resources (css, js, images)
                        requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll().
                // everyone can login and register
                        antMatchers("/", "/items/all/**").permitAll().
                        antMatchers("/users/login", "/users/register").anonymous().
                        antMatchers("/users/changepassword").authenticated().
                // pages available only for purchase department
                        antMatchers("/pages/purchases/**").hasAnyRole(UserRoleEnum.EMPLOYEE_PURCHASES.name(), UserRoleEnum.ADMIN.name()).
                // pages available only for purchase department
                        antMatchers("/pages/sales/**").hasAnyRole(UserRoleEnum.EMPLOYEE_SALES.name(), UserRoleEnum.ADMIN.name()).
                // pages available only for admins
                        antMatchers("/pages/admins/**").hasRole(UserRoleEnum.ADMIN.name()).
                //we permit the page below only only for admin users
//                        antMatchers("/pages/admins/statistics").hasRole(UserRoleEnum.ADMIN.toString()).
                // all other pages are available for logger in users
//                        antMatchers("/**").authenticated().
                anyRequest().authenticated().
                and().
                // configuration of form login
                        formLogin().
                // the custom login form
                        loginPage("/users/login").
                // the name of the username form field
                        usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY).
                // the name of the password form field
                        passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY).
                // where to go in case that the login is successful
                        defaultSuccessUrl("/").
                // where to go in case that the login failed
                        failureForwardUrl("/users/login-errorHandling").
                and().
                // configure logout
                        logout().
                // which is the logout url
                        logoutUrl("/users/logout").
                // invalidate the session and delete the cookies
                        invalidateHttpSession(true).
                deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/");

        http.csrf().csrfTokenRepository(csrfTokenRepository());

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new AppUserDetailsService(userRepository);
    }

    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");

        return repository;
    }
}










