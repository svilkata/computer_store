package bg.softuni.computerStore.model.binding.user;

import bg.softuni.computerStore.model.validation.emailUsername.UniqueUserEmail;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

public class EmployeeRegisterBindingDTO {
    @NotEmpty(message = "E-mail should be provided") //we override here the default error message
    @Email(message = "E-mail should be valid") //we override here the default error message
    @UniqueUserEmail(message = "Email already exists in the db. Please, think of another e-mail")  //we override here the default error message
    private String email;

    @NotBlank(message = "Username cannot be empty")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters inclusive")
    @UniqueUserEmail(message = "Username already exists in the db. Please, think of another e-mail")  //we override here the default error message
    private String username;

    @NotBlank(message = "First name cannot be empty")
    @Size(min = 2, max = 20, message = "First name must be between 3 and 20 characters inclusive")
    private String firstName;

    @NotBlank(message = "Last name cannot be empty")
    @Size(min = 2, max = 20, message = "Last name must be between 3 and 20 characters inclusive")
    private String lastName;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters inclusive")
    private String password;

    private Set<String> roles;

    public EmployeeRegisterBindingDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public EmployeeRegisterBindingDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public EmployeeRegisterBindingDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public EmployeeRegisterBindingDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public EmployeeRegisterBindingDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public EmployeeRegisterBindingDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public EmployeeRegisterBindingDTO setRoles(Set<String> roles) {
        this.roles = roles;
        return this;
    }
}
