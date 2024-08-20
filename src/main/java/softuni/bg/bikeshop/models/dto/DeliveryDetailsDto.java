package softuni.bg.bikeshop.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class DeliveryDetailsDto {
    @NotBlank(message = "Recipient name should not be empty!")
    @Size(min = 5,max = 40,message = "Full name should be between 5 and 40 characters!")
    private String recipientName;
    @NotBlank(message = "Street should not be empty!")
    @Size(min = 3,max = 40,message = "Street should be between 5 and 40 characters!")
    private String street;
    @NotBlank(message = "City should not be empty!")
    @Size(min = 3,max = 40,message = "City should be between 3 and 40 characters!")
    private String city;
    @NotBlank(message = "Postal code should not be empty!")
    @Size(min = 4,max = 4,message = "Postal code should be exactly 4 characters!")
    private String postalCode;
    @NotBlank
    @Pattern(regexp = "(0/91)?[7-9][0-9]{9}")
    private String phoneNumber;

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
