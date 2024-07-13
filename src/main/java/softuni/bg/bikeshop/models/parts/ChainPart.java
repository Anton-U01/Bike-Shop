package softuni.bg.bikeshop.models.parts;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "chains_parts")
@DiscriminatorValue("CHAIN")
public class ChainPart extends Part{
    @Column(nullable = false)
    private int speedsCount;
    @Column(nullable = false)

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
