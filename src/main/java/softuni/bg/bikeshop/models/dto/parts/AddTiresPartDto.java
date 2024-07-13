package softuni.bg.bikeshop.models.dto.parts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AddTiresPartDto extends AddPartDto{
    @NotNull(message = "Count of speed should not be empty!")
    @Positive(message = "Count of speed should be positive number!")
    private int size;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
