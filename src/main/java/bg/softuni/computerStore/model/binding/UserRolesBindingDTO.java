package bg.softuni.computerStore.model.binding;

import java.util.List;

public class UserRolesBindingDTO {
    private String username;
    private List<String> roles;

    public UserRolesBindingDTO() {
    }

    public String getUsername() {
        return username;
    }

    public UserRolesBindingDTO setUsername(String username) {
        this.username = username;
        return this;
    }

    public List<String> getRoles() {
        return roles;
    }

    public UserRolesBindingDTO setRoles(List<String> roles) {
        this.roles = roles;
        return this;
    }
}
