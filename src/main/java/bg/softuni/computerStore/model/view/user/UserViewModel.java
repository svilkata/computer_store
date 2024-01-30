package bg.softuni.computerStore.model.view.user;

public class UserViewModel {
    private String username;
    //TODO: add photo of user

    public UserViewModel() {
    }

    public String getUsername() {
        return username;
    }

    public UserViewModel setUsername(String username) {
        this.username = username;
        return this;
    }
}
