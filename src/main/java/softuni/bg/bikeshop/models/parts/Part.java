package softuni.bg.bikeshop.models.parts;

import jakarta.persistence.*;
import softuni.bg.bikeshop.models.Product;

@Entity
@Table(name = "parts")
@Inheritance(strategy = InheritanceType.JOINED)
public class Part extends Product {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PartType partType;

    @Column(nullable = false)
    private String manufacturer;

    public PartType getPartType() {
        return partType;
    }

    public void setPartType(PartType partType) {
        this.partType = partType;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }
}
