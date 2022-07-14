package bg.softuni.computer_store.model.binding;

import bg.softuni.computer_store.model.validation.email.UniqueUserEmail;
import bg.softuni.computer_store.model.validation.password.FieldMatch;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@FieldMatch(firstField = "password",
        secondField = "confirmPassword",
        message = "Passwords do not match" //we override here the default error message
)
public class UserRegisterDTO {
    @NotEmpty(message = "User email should be provided") //we override here the default error message
    @Email(message = "User email should be valid") //we override here the default error message
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

    @NotBlank(message = "confirmPassword cannot be empty")
    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters inclusive")
    private String confirmPassword;

    public String getFirstName() {
        return firstName;
    }

    public UserRegisterDTO setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public UserRegisterDTO setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public UserRegisterDTO setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public UserRegisterDTO setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public UserRegisterDTO setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public UserRegisterDTO setUsername(String username) {
        this.username = username;
        return this;
    }
}
