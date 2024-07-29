package softuni.bg.bikeshop.models.dto.parts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.HashMap;
import java.util.Map;

public class EditPartDto {
    @NotNull
    private Long id;
    @NotBlank(message = "Name should not be empty!")
    @Size(min = 4,max = 30,message = "Name should be between 4 and 30 characters!")
    private String name;
    @Size(min = 20,max = 200,message = "Description should be between 20 and 200 characters!")
    private String description;
    @NotNull(message = "Price should not be empty!")
    @Positive(message = "Price should be a positive number!")
    private double price;
    @NotNull(message = "Quantity should not be empty!")
    @Positive(message = "Quantity should be a positive number!")
    private Integer quantity;
    @NotBlank(message = "Name should not be empty!")
    @Size(min = 4,max = 30,message = "Name should be between 4 and 30 characters!")
    private String manufacturer;
    @NotBlank(message = "Type should not be empty!")
    private String type;

    private Map<String, Object> dynamicFields;


    public EditPartDto() {
        dynamicFields = new HashMap<>();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, Object> getDynamicFields() {
        return dynamicFields;
    }

    public void setDynamicFields(Map<String, Object> dynamicFields) {
        this.dynamicFields = dynamicFields;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

