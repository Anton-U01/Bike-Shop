package softuni.bg.bikeshop.service;

import softuni.bg.bikeshop.models.Product;
import softuni.bg.bikeshop.models.orders.*;

import java.time.LocalDate;
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

    String getPublicKey();

    void setOrderToCompleted(String username);

    List<OrderViewDto> getCompletedAndDeliveredOrdersOfUser(String name);

    void checkAndUpdateDeliveryDetails(String username, DeliveryDetailsDto deliveryDetailsDto);

    List<OrderWithDeliveryDetailsDto> getAllCompletedAndDeliveredOrders();

    void setOrderToDelivered(Long id);

    void archive(Long id);

    List<OrderWithDeliveryDetailsDto> getArchivedOrders();

    void restoreOrderFromArchive(Long id);

    List<Order> findArchivedOrdersOlderThan(LocalDate sixtyDaysAgo);

    void deleteOrder(long id);
}
