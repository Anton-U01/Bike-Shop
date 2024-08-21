package softuni.bg.bikeshop.service;

import org.springframework.stereotype.Service;
import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.dto.OrderItemView;
import softuni.bg.bikeshop.models.orders.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Order getMyBag(String username);

    void addProductToBag(Product product, Integer quantity, String name);

    int getMyBagSize(String username);

    List<OrderItemView> getMyBagItems(Order myBag);

    void updateQuantities(Map<String, String> quantities, Order myBag);

    void removeItemFromBag(Order myBag, Long itemId);
}
