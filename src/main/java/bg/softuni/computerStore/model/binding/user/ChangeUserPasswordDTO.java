package bg.softuni.computerStore.model.binding.user;

import bg.softuni.computerStore.model.validation.password.FieldMatch;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@FieldMatch(firstField = "newPassword",
        secondField = "confirmNewPassword",
        message = "New passwords do not match" //we override here the default errorHandling message
)
public class ChangeUserPasswordDTO {
    //we take/display the username automatically
    private String username;
    //we tell the user to enter his/her existing password
    private String currentPassword;

    @NotBlank(message = "New password cannot be empty")
    @Size(min = 3, max = 20, message = "New password must be between 3 and 20 characters inclusive")
    private String newPassword;

    @NotBlank(message = "confirmNewPassword cannot be empty")
    @Size(min = 3, max = 20, message = "confirmNewPassword must be between 3 and 20 characters inclusive")
    private String confirmNewPassword;

    public ChangeUserPasswordDTO() {
    }

    public String getUsername() {
        return username;
    }

    public ChangeUserPasswordDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public ChangeUserPasswordDTO setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public ChangeUserPasswordDTO setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public ChangeUserPasswordDTO setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
        return this;
    }
}
