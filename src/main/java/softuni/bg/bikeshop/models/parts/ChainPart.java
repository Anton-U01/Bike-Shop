package softuni.bg.bikeshop.models.parts;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "chains_parts")
public class ChainPart extends Part{
    private int speedsCount;

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
