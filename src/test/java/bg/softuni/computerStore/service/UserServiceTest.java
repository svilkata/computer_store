package bg.softuni.computerStore.service;

import bg.softuni.computerStore.model.binding.user.UserRegisterBindingDTO;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.entity.users.UserRoleEntity;
import bg.softuni.computerStore.model.view.user.UserViewModel;
import bg.softuni.computerStore.repository.users.UserRepository;
import bg.softuni.computerStore.repository.users.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    private String adminPass = "11111";

//    private ModelMapper modelMapper;
//    private BasketService basketService;

    @BeforeEach
    void init() {
        this.userService.init();
    }

    @Test
    void registerUserAndAutoLoginTest() {
        UserRegisterBindingDTO newUserToRegisterBindingDTO = new UserRegisterBindingDTO();
        newUserToRegisterBindingDTO
                .setUsername("pesh")
                .setEmail("pesho@abv.wow")
                .setFirstName("Pesho")
                .setLastName("Peshov")
                .setPassword(passwordEncoder.encode(adminPass));

        Long actual = this.userService.registerUserAndAutoLogin(newUserToRegisterBindingDTO);

        Optional<UserEntity> pesh = this.userRepository.findByUsername("pesh");

        assertEquals(pesh.get().getId(), actual);
    }

    @Test
    void getEmployeeUsersTest() {
        List<UserViewModel> employeeUsers = this.userService.getEmployeeUsers();

        List<UserEntity> employees = userRepository.findAllEmployeeUsers(); //more than 1 role
        UserRoleEntity userRoleEntityAdmin = userRoleRepository.getById(1L);
        List<UserViewModel> result = employees.stream()
                .filter(u -> !u.getUserRoles().contains(userRoleEntityAdmin))
                .map(u -> modelMapper.map(u, UserViewModel.class))
                .collect(Collectors.toList());

        assertEquals(result.size(), employeeUsers.size());
    }

    @AfterEach
    void clearUp(){

    }

//    @Test
//    void setEmployeeRolesTest() {
//    }

//    @Test
//    void disableAdminRoleForCurrentAdminUser() {
//    }

//    @Test
//    void registerEmployee() {
//    }

//    @Test
//    void changeCurrentUserPassword() {
//    }
}