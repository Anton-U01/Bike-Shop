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
    @NotBlank(message = "Country should not be empty!")
    @Size(min = 5,max = 40,message = "Country should be between 5 and 40 characters!")
    private String country;
    @NotBlank(message = "City should not be empty!")
    @Size(min = 3,max = 40,message = "City should be between 3 and 40 characters!")
    private String city;
    @NotBlank(message = "Street should not be empty!")
    @Size(min = 3,max = 40,message = "Street should be between 5 and 40 characters!")
    private String street;
    @NotBlank(message = "Postal code should not be empty!")
    @Size(min = 4,max = 4,message = "Postal code should be exactly 4 characters!")
    private String postalCode;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}
