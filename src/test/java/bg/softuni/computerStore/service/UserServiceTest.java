package bg.softuni.computerStore.service;

import bg.softuni.computerStore.model.binding.user.ChangeUserPasswordDTO;
import bg.softuni.computerStore.model.binding.user.EmployeeRegisterBindingDTO;
import bg.softuni.computerStore.model.binding.user.UserRegisterBindingDTO;
import bg.softuni.computerStore.model.binding.user.UserRolesBindingDTO;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.entity.users.UserRoleEntity;
import bg.softuni.computerStore.model.view.user.UserViewModel;
import bg.softuni.computerStore.repository.users.UserRepository;
import bg.softuni.computerStore.repository.users.UserRoleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
import java.util.Set;
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

    @BeforeEach
    void init() {
        this.userService.init();
    }

    @AfterEach
    void clearUp(){

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
        List<UserViewModel> actual = this.userService.getEmployeeUsers();

        List<UserEntity> employees = userRepository.findAllEmployeeUsers(); //more than 1 role
        UserRoleEntity userRoleEntityAdmin = userRoleRepository.getById(1L);
        List<UserViewModel> result = employees.stream()
                .filter(u -> !u.getUserRoles().contains(userRoleEntityAdmin))
                .map(u -> modelMapper.map(u, UserViewModel.class))
                .collect(Collectors.toList());

        assertEquals(result.size(), actual.size());
    }

    @Test
    void setEmployeeRolesTestSuccessfull() {
        Optional<UserEntity> purchaseUserOpt = this.userRepository.findByUsername("purchase");
        UserEntity purchaseUser = purchaseUserOpt.get();

        //2 roles in the beginning
        Assertions.assertEquals(2L, purchaseUser.getUserRoles().size());

        UserRolesBindingDTO userRolesBindingDTO = new UserRolesBindingDTO();
        userRolesBindingDTO
                .setUsername("purchase")
                .setRoles(List.of("EMPLOYEE_PURCHASES", "CUSTOMER", "EMPLOYEE_SALES"));

        this.userService.setEmployeeRoles(userRolesBindingDTO);

        purchaseUserOpt = this.userRepository.findByUsername("purchase");
        purchaseUser = purchaseUserOpt.get();

        //from 2 roles to 3 roles
        Assertions.assertEquals(3, purchaseUser.getUserRoles().size());
    }

    @Test
    void setEmployeeRolesTestWhenWrongUserRoleGiven() {
        Optional<UserEntity> purchaseUserOpt = this.userRepository.findByUsername("sales");
        UserEntity purchaseUser = purchaseUserOpt.get();

        //2 roles in the beginning
        Assertions.assertEquals(2L, purchaseUser.getUserRoles().size());

        UserRolesBindingDTO userRolesBindingDTO = new UserRolesBindingDTO();
        userRolesBindingDTO
                .setUsername("sales")
                .setRoles(List.of("EMPLOYEE_PURCHASES", "CUS", "EMPLOYEE_SALES"));

        Assertions.assertThrows(IllegalArgumentException.class,
                () -> this.userService.setEmployeeRoles(userRolesBindingDTO));
    }

    @Test
    void disableAdminRoleForCurrentAdminUserTestSuccessfull() {
        Optional<UserEntity> adminOpt = this.userRepository.findByUsername("admin");
        UserEntity admin = adminOpt.get();

        //4 roles oin the beginning
        Assertions.assertEquals(4L, admin.getUserRoles().size());

        this.userService.disableAdminRoleForCurrentAdminUser();

        adminOpt = this.userRepository.findByUsername("admin");
        admin = adminOpt.get();

        //from 4 roles to 3 roles
        Assertions.assertEquals(3, admin.getUserRoles().size());
    }

    @Test
    void registerEmployeeTest() {
        EmployeeRegisterBindingDTO employeeRegisterBindingDTO = new EmployeeRegisterBindingDTO();
        employeeRegisterBindingDTO
                .setFirstName("Dragan")
                .setLastName("Draganov")
                .setUsername("dragan")
                .setEmail("dragan@abv.bg")
                .setPassword("11111")
                .setRoles(Set.of("CUSTOMER"));

        this.userService.registerEmployee(employeeRegisterBindingDTO);

        Optional<UserEntity> dragan = this.userRepository.findByUsername("dragan");
        assertEquals(true, dragan.isPresent());
    }

    @Test
    void changeCurrentUserPasswordTest() {
        ChangeUserPasswordDTO changeUserPasswordDTO = new ChangeUserPasswordDTO();
        changeUserPasswordDTO
                .setUsername("sales")
                .setCurrentPassword("11111")
                .setNewPassword("22222")
                .setConfirmNewPassword("22222");

        this.userService.changeCurrentUserPassword(changeUserPasswordDTO);

        Optional<UserEntity> salesOpt = this.userRepository.findByUsername("sales");
        UserEntity sales = salesOpt.get();

        boolean checkPasswords = passwordEncoder.matches("22222", sales.getPassword());

        assertEquals(true, checkPasswords);
    }

    @Test
    void getCountOfRegisteredUsersTest() {
        int result = this.userService.getCountOfRegisteredUsers();
        int expected = this.userRepository.findAll().size();

        Assertions.assertEquals(expected, result);
    }
}