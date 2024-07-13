package softuni.bg.bikeshop.models.parts;


import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "frame_parts")
@DiscriminatorValue("FRAME")
public class FramePart extends Part{
    @Column(nullable = false)
    private String material;
    @Column(nullable = false)
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
