package softuni.bg.bikeshop.service;

import softuni.bg.bikeshop.models.orders.OrderItem;

import java.util.List;

public interface EmailSenderService {
    void sendOrderConfirmationEmail(String to, String orderId, List<OrderItem> orderItems, double totalAmount);
}
