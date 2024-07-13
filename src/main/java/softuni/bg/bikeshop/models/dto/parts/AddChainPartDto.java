package softuni.bg.bikeshop.models.dto.parts;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class AddChainPartDto extends AddPartDto{
    @NotNull(message = "Count of speed should not be empty!")
    @Positive(message = "Count of speed should be positive number!")
    private int speedsCount;
    @NotNull(message = "Count of chains should not be empty!")
    @Positive(message = "Count of chains should be positive number!")
    private int chainLinks;

    public int getSpeedsCount() {
        return speedsCount;
    }

    public void setSpeedsCount(int speedsCount) {
        this.speedsCount = speedsCount;
    }

    public int getChainLinks() {
        return chainLinks;
    }

    public void setChainLinks(int chainLinks) {
        this.chainLinks = chainLinks;
    }
}
