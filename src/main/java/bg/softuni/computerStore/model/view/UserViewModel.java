package bg.softuni.computerStore.model.view;

public class UserViewModel {
    private String username;
//    private String imageUrl;


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
