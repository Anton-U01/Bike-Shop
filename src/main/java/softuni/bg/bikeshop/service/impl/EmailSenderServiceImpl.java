package softuni.bg.bikeshop.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import softuni.bg.bikeshop.models.orders.OrderItem;
import softuni.bg.bikeshop.service.EmailSenderService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender javaMailSender;
    @Value("${EMAIL}")
    private String from;


    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(to);
        message.setText(body);
        message.setSubject(subject);

        javaMailSender.send(message);
    }
    @Override
    public void sendOrderConfirmationEmail(String to, String orderId, List<OrderItem> orderItems, double totalAmount) {
        StringBuilder body = new StringBuilder();
        body.append("Dear Customer,\n\n");
        body.append("Thank you for your order!\n\n");
        body.append("Order ID: ").append(orderId).append("\n");
        body.append("Order Details:\n");

        for (OrderItem item : orderItems) {
            body.append("Product Name: ").append(item.getProductName()).append("\n");
            body.append("Quantity: ").append(item.getQuantity()).append("\n");
            body.append("Price: $").append(item.getProductPrice()).append("\n");
            body.append("Total: $").append(item.getQuantity() * item.getProductPrice()).append("\n\n");
        }

        body.append("Total Amount: $").append(totalAmount).append("\n\n");
        body.append("Thank you for shopping with us!\n");
        body.append("Best Regards,\n");
        body.append("BikeShopStore");

        sendEmail(to, "Order Confirmation - " + orderId, body.toString());
    }

    @Override
    public void sendOrderDeliveredEmail(String to, String orderId, String recipientName) {
        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(recipientName).append(",\n\n");
        body.append("We are pleased to inform you that your order has been successfully delivered.\n\n");
        body.append("Order ID: ").append(orderId).append("\n");
        body.append("Delivery Date: ").append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").format(LocalDateTime.now())).append("\n\n");
        body.append("Thank you for choosing BikeShopStore!\n");
        body.append("We hope you enjoy your purchase.\n\n");
        body.append("Best Regards,\n");
        body.append("BikeShopStore");

        sendEmail(to, "Order Delivered - " + orderId, body.toString());
    }
}
