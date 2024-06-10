package softuni.bg.bikeshop.models;

import jakarta.persistence.*;

@Entity
@Table(name = "inventory")
public class Inventory extends BaseEntity{
    @OneToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private Product product;
    @Column(nullable = false)
    private int quantity;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
