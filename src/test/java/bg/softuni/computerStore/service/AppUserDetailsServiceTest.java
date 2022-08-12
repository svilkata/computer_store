package bg.softuni.computerStore.service;

import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.entity.users.UserRoleEntity;
import bg.softuni.computerStore.model.enums.UserRoleEnum;
import bg.softuni.computerStore.repository.users.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public class AppUserDetailsServiceTest {
    private UserEntity testUser;
    private UserRoleEntity adminRole, customerRole;
    private AppUserDetailsService testAppUserDetailsService;

    @Mock
    private UserRepository mockUserRepository;

    @BeforeEach
    void init() {
        testAppUserDetailsService = new AppUserDetailsService(mockUserRepository);

        adminRole = new UserRoleEntity().setUserRole(UserRoleEnum.ADMIN);
        customerRole = new UserRoleEntity().setUserRole(UserRoleEnum.CUSTOMER);

        testUser = new UserEntity()
                .setFirstName("Svilen")
                .setLastName("Velikov")
                .setUsername("admin")
                .setEmail("svilkata@abv.bg")
                .setPassword("11111")
                .setUserRoles(Set.of(adminRole, customerRole));
    }

    @Test
    void testUserNotFound() {
        Assertions.assertThrows(
                UsernameNotFoundException.class,
                () -> testAppUserDetailsService.loadUserByUsername("invalid_username"));
    }

    @Test
    void testUserFound() {
        //Arrange
        Mockito.when(mockUserRepository.findByUsername(testUser.getUsername()))
                .thenReturn(Optional.of(testUser));

        //Act
        UserDetails actual = testAppUserDetailsService.loadUserByUsername(testUser.getUsername());

        //Assert
        Assertions.assertEquals(actual.getUsername(), testUser.getUsername());

        String actualRoles = actual.getAuthorities().stream().map(ga -> ga.getAuthority())
                .collect(Collectors.joining(", "));
        String expectedRoles = "ROLE_ADMIN, ROLE_CUSTOMER";
        Assertions.assertEquals(expectedRoles, actualRoles);


    }
}
