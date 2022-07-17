package bg.softuni.computerStore.model.binding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserLoginBindingDto {
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters inclusive")
    @NotBlank(message = "Username cannot be empty")
    private String username;

    @Size(min = 3, max = 20, message = "Password must be between 3 and 20 characters inclusive")
    @NotBlank(message = "Password cannot be empty")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserLoginBindingDto() {
    }

    @Override
    public String toString() {
        return "UserLoginDto{" +
                "username='" + username + '\'' +
                ", password='" + (password != null ? "[PROVIDED]" : null) + '\'' +  //паролата не я печатаме!!!
                '}';
    }
}
