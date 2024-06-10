package softuni.bg.bikeshop.models;

import jakarta.persistence.*;

@Entity
@Table(name = "bikes")
public class Bike extends Product{
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BikeType type;

    @Column(nullable = false)
    private String frame;
    @Column(nullable = false)
    private String brakes;
    @Column(nullable = false)
    private double wheelsSize;
    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public BikeType getType() {
        return type;
    }

    public void setType(BikeType type) {
        this.type = type;
    }

    public String getBrakes() {
        return brakes;
    }

    public void setBrakes(String brakes) {
        this.brakes = brakes;
    }

    public double getWheelsSize() {
        return wheelsSize;
    }

    public void setWheelsSize(double wheelsSize) {
        this.wheelsSize = wheelsSize;
    }
}
