package bg.softuni.computer_store.service;

import bg.softuni.computer_store.model.entity.UserEntity;
import bg.softuni.computer_store.model.entity.UserRoleEntity;
import bg.softuni.computer_store.repository.UserRepository;
import bg.softuni.computer_store.user.AppUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

// NOTE: This is not annotated as @Service, because we will return it as a bean.
public class AppUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public AppUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        return userRepository.
                findByUsername(username).
                map(this::mapUserEntityToUserDetails).
                orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found!"));
    }

    private UserDetails mapUserEntityToUserDetails(UserEntity userEntity) {
//        return
//                User.builder().
//                        username(userEntity.getUsername()).
//                        password(userEntity.getPassword()).
//                        authorities(userEntity.
//                                getUserRoles().
//                                stream().
//                                map(this::mapRole).
//                                toList()).
//                        build();
        return new AppUser(
                userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getFirstName(),
                userEntity.getLastName(),
                userEntity.
                        getUserRoles().
                        stream().
                        map(this::mapRole).
                        toList()
        );
    }

    private GrantedAuthority mapRole(UserRoleEntity userRole) {
        return new SimpleGrantedAuthority("ROLE_" +
                userRole.
                        getUserRole().name());
    }
}
