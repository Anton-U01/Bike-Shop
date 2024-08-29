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
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private String productName;
    @Column(columnDefinition = "TEXT")
    private String productDescription;
    @Column(nullable = false)
    private double productPrice;
    @Column(nullable = false,columnDefinition = "LONGTEXT")
    private String productPictureUrl;
    @Column(nullable = false)
    private int productQuantity;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductPictureUrl() {
        return productPictureUrl;
    }

    public void setProductPictureUrl(String productPictureUrl) {
        this.productPictureUrl = productPictureUrl;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
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
