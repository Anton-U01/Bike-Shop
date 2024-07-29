package softuni.bg.bikeshop.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class EditBikeDto {
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
    @NotBlank(message = "Type should not be empty!")
    private String type;
    @NotBlank(message = "Frame should not be empty!")
    @Size(min = 4,max = 30,message = "Frame should be between 4 and 30 characters!")
    private String frame;
    @NotBlank(message = "Brakes should not be empty!")
    @Size(min = 4,max = 30,message = "Brakes should be between 4 and 30 characters!")
    private String brakes;
    @NotNull
    @Positive(message = "Wheels size should be positive number!")
    private double wheelsSize;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public String getBrakes() {
        return brakes;
    }

    public void setBrakes(String brakes) {
        this.brakes = brakes;
    }

    public double getWheelsSize() {
        return wheelsSize;
    }

    public void setWheelsSize(double wheelsSize) {
        this.wheelsSize = wheelsSize;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
