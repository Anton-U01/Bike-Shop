package softuni.bg.bikeshop.models.dto.parts;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class AddFramePartDto extends AddPartDto{
    @NotBlank(message = "Material should not be empty!")
    @Size(min = 4,max = 30,message = "Material should be between 4 and 30 characters!")
    private String material;
    @NotNull(message = "Price should not be empty!")
    @Positive(message = "Price should be a positive number!")
    private double weight;

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }
}
