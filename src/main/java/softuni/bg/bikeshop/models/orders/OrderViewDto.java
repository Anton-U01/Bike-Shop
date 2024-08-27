package softuni.bg.bikeshop.models.orders;

import java.util.List;

public class OrderViewDto {
        public long id;
        public String orderDate;
        public double totalAmount;
        public String status;
        public List<OrderItemView> items;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<OrderItemView> getItems() {
        return items;
    }

    public void setItems(List<OrderItemView> items) {
        this.items = items;
    }
}
