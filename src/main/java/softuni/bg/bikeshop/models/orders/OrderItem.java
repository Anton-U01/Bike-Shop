package softuni.bg.bikeshop.models.orders;

import jakarta.persistence.*;
import softuni.bg.bikeshop.models.BaseEntity;
import softuni.bg.bikeshop.models.Product;

@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id")
    private Order order;
    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private Product product;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private double price;

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
