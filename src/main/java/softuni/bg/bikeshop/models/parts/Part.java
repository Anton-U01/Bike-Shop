package softuni.bg.bikeshop.models.parts;

import jakarta.persistence.*;
import softuni.bg.bikeshop.models.Product;

@Entity
@Table(name = "parts")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class Part extends Product {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartType type;

    @Column(nullable = false)
    private String manufacturer;

    public PartType getType() {
        return type;
    }

    public void setType(PartType type) {
        this.type = type;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
