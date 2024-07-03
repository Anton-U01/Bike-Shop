package softuni.bg.bikeshop.models.dto;


import jakarta.validation.constraints.*;

public class UserRegisterDto {
    @NotBlank(message = "Username should not be empty!")
    @Size(min = 4,max = 20,message = "Username should be between 4 and 20 characters!")
    private String username;
    @NotBlank(message = "Full name should not be empty!")
    @Size(min = 5,max = 40,message = "Full name should be between 5 and 40 characters!")
    private String fullName;
    @NotNull(message = "Age should not be empty!")
    @Positive(message = "Age should be positive number!")
    private int age;
    @NotBlank(message = "Email should not be empty!")
    @Email(message = "Email should be a valid email!")
    private String email;
    @NotBlank(message = "Password should be not empty!")
    @Size(min = 5,max = 20,message = "Password must be between 5 and 20 characters!")
    private String password;
    @NotBlank(message = "Password should be not empty!")
    @Size(min = 5,max = 20,message = "Password must be between 5 and 20 characters!")
    private String confirmPassword;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
