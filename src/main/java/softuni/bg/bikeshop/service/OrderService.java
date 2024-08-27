package softuni.bg.bikeshop.service;

import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.orders.DeliveryDetailsDto;
import softuni.bg.bikeshop.models.orders.OrderItemView;
import softuni.bg.bikeshop.models.orders.Order;
import softuni.bg.bikeshop.models.orders.OrderViewDto;

import java.util.List;
import java.util.Map;

public interface OrderService {

    Order getMyBag(String username);

    void addProductToBag(Product product, Integer quantity, String name);

    int getMyBagSize(String username);

    List<OrderItemView> getMyBagItems(Order myBag);

    void updateQuantities(Map<String, String> quantities, Order myBag);

    void removeItemFromBag(Order myBag, Long itemId);

    DeliveryDetailsDto getUserDeliveryDetails(String username);

    void saveDeliveryDetails(String name, DeliveryDetailsDto deliveryDetailsDto);

    boolean myBagHasAlreadyDeliveryDetails(String username);
    String getPublicKey();

    void setOrderToCompleted(String username);

    List<OrderViewDto> getCompletedOrders(String name);
}
