package softuni.bg.bikeshop.models.parts;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tires_parts")
@DiscriminatorValue("TIRES")
public class TiresPart extends Part{
    @Column(nullable = false)
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
