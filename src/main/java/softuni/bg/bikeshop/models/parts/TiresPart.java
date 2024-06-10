package softuni.bg.bikeshop.models.parts;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "tires_parts")
public class TiresPart extends Part{
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
