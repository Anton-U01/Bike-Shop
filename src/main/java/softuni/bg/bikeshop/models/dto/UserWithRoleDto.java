package softuni.bg.bikeshop.models.dto;

import jakarta.validation.constraints.NotEmpty;

public class UserWithRoleDto {
    private String username;
    @NotEmpty
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
