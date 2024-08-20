package softuni.bg.bikeshop.models.orders;

import jakarta.persistence.*;
import org.springframework.transaction.annotation.Transactional;
import softuni.bg.bikeshop.models.BaseEntity;
import softuni.bg.bikeshop.models.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;
    @Column(name = "order_date",nullable = false)
    private LocalDate orderDate;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items;
    @Column(name = "total_amount",nullable = false)
    private double totalAmount;
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    public Order() {
        items = new ArrayList<>();
        totalAmount = 0;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderItem> getOrderItems() {
        return items;
    }

    public void setOrderItems(List<OrderItem> items) {
        this.items = items;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
