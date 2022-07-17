package bg.softuni.computerStore.service;

import bg.softuni.computerStore.init.InitializableService;
import bg.softuni.computerStore.model.binding.UserRegisterBindingDTO;
import bg.softuni.computerStore.model.binding.UserRolesBindingDTO;
import bg.softuni.computerStore.model.entity.users.UserEntity;
import bg.softuni.computerStore.model.entity.users.UserRoleEntity;
import bg.softuni.computerStore.model.enums.UserRoleEnum;
import bg.softuni.computerStore.model.view.UserViewModel;
import bg.softuni.computerStore.repository.users.UserRepository;
import bg.softuni.computerStore.repository.users.UserRoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService implements InitializableService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService appUserDetailsService;
    private final String adminPass;
    private final ModelMapper modelMapper;


    public UserService(
            UserRepository userRepository, UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder,
            UserDetailsService appUserDetailsService,
            @Value("${app.default.admin.password}") String adminPass, ModelMapper modelMapper) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.appUserDetailsService = appUserDetailsService;
        this.adminPass = adminPass;
        this.modelMapper = modelMapper;
    }

    @Override
    public void init() {
        if (userRepository.count() == 0 && userRoleRepository.count() == 0) {
            UserRoleEntity adminRole = new UserRoleEntity().setUserRole(UserRoleEnum.ADMIN);
            UserRoleEntity purchaseRole = new UserRoleEntity().setUserRole(UserRoleEnum.EMPLOYEE_PURCHASES);
            UserRoleEntity salesRole = new UserRoleEntity().setUserRole(UserRoleEnum.EMPLOYEE_SALES);
            UserRoleEntity customerRole = new UserRoleEntity().setUserRole(UserRoleEnum.CUSTOMER);

            //тук SQL базата му слага пореден ID номер
            adminRole = userRoleRepository.save(adminRole);
            purchaseRole = userRoleRepository.save(purchaseRole);
            salesRole = userRoleRepository.save(salesRole);
            customerRole = userRoleRepository.save(customerRole);

            initAdmin(List.of(adminRole, purchaseRole, salesRole, customerRole));
            initEmployeePurchases(List.of(purchaseRole, customerRole));
            initEmployeeSales(List.of(salesRole, customerRole));

            initCustomer(List.of(customerRole));
        }
    }

    private void initAdmin(List<UserRoleEntity> roles) {
        UserEntity admin = new UserEntity()
                .setUserRoles(roles)
                .setFirstName("Svilen")
                .setLastName("Velikov")
                .setEmail("svilkata_sh@abv.bg")
                .setUsername("admin")
                .setPassword(passwordEncoder.encode(adminPass));

        userRepository.save(admin);
    }

    private void initEmployeePurchases(List<UserRoleEntity> roles) {
        UserEntity employeePurchases = new UserEntity().
                setUserRoles(roles).
                setFirstName("Покукпо").
                setLastName("Покупков").
                setEmail("purchase@pcstore.com").
                setUsername("purchaseGuy").
                setPassword(passwordEncoder.encode(adminPass));

        userRepository.save(employeePurchases);
    }

    private void initEmployeeSales(List<UserRoleEntity> roles) {
        UserEntity employeeSales = new UserEntity().
                setUserRoles(roles).
                setFirstName("Продавач").
                setLastName("Продавачов").
                setEmail("sales@pcstore.com").
                setUsername("salesGuy").
                setPassword(passwordEncoder.encode(adminPass));

        userRepository.save(employeeSales);
    }


    private void initCustomer(List<UserRoleEntity> roles) {
        UserEntity customer = new UserEntity().
                setUserRoles(roles).
                setFirstName("User").
                setLastName("Userov").
                setEmail("users@example.com").
                setUsername("users").
                setPassword(passwordEncoder.encode(adminPass));

        userRepository.save(customer);
    }

    public void registerUserAndAutoLogin(UserRegisterBindingDTO userRegisterBindingDTO) {
        //The customer user role
        UserRoleEntity userRole = userRoleRepository.findById(4L).get();

        UserEntity newCustomer =
                new UserEntity().
                        setUserRoles(List.of(userRole)).
                        setUsername(userRegisterBindingDTO.getUsername()).
                        setEmail(userRegisterBindingDTO.getEmail()).
                        setFirstName(userRegisterBindingDTO.getFirstName()).
                        setLastName(userRegisterBindingDTO.getLastName()).
                        setPassword(passwordEncoder.encode(userRegisterBindingDTO.getPassword()));

        userRepository.save(newCustomer);

        //this is the Spring representation of a User - after register, we AUTO log-in the users directly
        UserDetails userDetails =
                appUserDetailsService.loadUserByUsername(newCustomer.getUsername());

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

    public List<UserViewModel> getEmployeeUsers() {
        //Each employee user has more than 1 role! Each customer has only 1 role!
        List<UserEntity> employees = userRepository.findAllEmployeeUsers(); //more than 1 role
        UserRoleEntity userRoleEntityAdmin = userRoleRepository.getById(1L);

        List<UserViewModel> result = employees.stream()
                .filter(u -> !u.getUserRoles().contains(userRoleEntityAdmin))
                .map(u -> modelMapper.map(u, UserViewModel.class))
                .collect(Collectors.toList());

        return result;
    }

    public void setEmployeeRoles(UserRolesBindingDTO userRolesBindingDTO) {
        UserEntity user = userRepository.findByUsername(userRolesBindingDTO.getUsername()).orElseThrow();
        user.getUserRoles().clear();

        userRolesBindingDTO.getRoles()
                .forEach(r -> {
                    UserRoleEntity userRole = userRoleRepository.
                            findByUserRole(UserRoleEnum.valueOf(r)).orElseThrow(
                                    () -> new IllegalStateException("Employee user Role not found. Please seed the roles."));

                    user.getUserRoles().add(userRole);
                });

        userRepository.save(user);
    }


    public void disableAdminRoleForCurrentAdminUser() {
        UserEntity disablingCurrentAdminUser = this.userRepository.getCurrentAdminUser();
        UserRoleEntity userRoleEntityAdmin = userRoleRepository.getById(1L);

        disablingCurrentAdminUser.getUserRoles().remove(userRoleEntityAdmin);
        userRepository.save(disablingCurrentAdminUser);
        return;
    }
}
