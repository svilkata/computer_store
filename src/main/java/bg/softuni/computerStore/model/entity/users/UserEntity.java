package bg.softuni.computerStore.model.entity.users;

import bg.softuni.computerStore.model.entity.BaseEntity;
import bg.softuni.computerStore.model.entity.orders.BasketOrderEntity;
import bg.softuni.computerStore.model.entity.orders.FinalOrderEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "is_active")
    private boolean isActive;

    @Column(name = "image_url")
    private String imageUrl;

    //    @ManyToMany
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<UserRoleEntity> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private List<BasketOrderEntity> basketsOfUser;

    @OneToMany(mappedBy = "user")
    private List<FinalOrderEntity> userOrders;

    public String getEmail() {
        return email;
    }

    public UserEntity setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserEntity setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserEntity setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserEntity setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public boolean isActive() {
        return isActive;
    }

    public UserEntity setActive(boolean active) {
        isActive = active;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public UserEntity setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Set<UserRoleEntity> getUserRoles() {
        return userRoles;
    }

    public UserEntity setUserRoles(Set<UserRoleEntity> userRoles) {
        this.userRoles = userRoles;
        return this;
    }

    public UserEntity addRole(UserRoleEntity userRole) {
        this.userRoles.add(userRole);
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserEntity setUsername(String username) {
        this.username = username;
        return this;
    }

    public List<BasketOrderEntity> getBasketsOfUser() {
        return basketsOfUser;
    }

    public UserEntity setBasketsOfUser(List<BasketOrderEntity> basketsOfUser) {
        this.basketsOfUser = basketsOfUser;
        return this;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "emailUsername='" + username + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", isActive=" + isActive +
                ", imageUrl='" + imageUrl + '\'' +
                ", userRoles=" + userRoles +
                '}';
    }

    public List<FinalOrderEntity> getUserOrders() {
        return userOrders;
    }

    public UserEntity setUserOrders(List<FinalOrderEntity> userOrders) {
        this.userOrders = userOrders;
        return this;
    }
}
