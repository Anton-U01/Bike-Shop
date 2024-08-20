package softuni.bg.bikeshop.models.dto;

import java.util.HashMap;
import java.util.Map;

public class BagUpdateDto {
    private Map<Long,Integer> quantities;

    public BagUpdateDto() {
        quantities = new HashMap<>();
    }

    public Map<Long, Integer> getQuantities() {
        return quantities;
    }
}
