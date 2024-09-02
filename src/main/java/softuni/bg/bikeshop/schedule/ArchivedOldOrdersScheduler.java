package softuni.bg.bikeshop.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import softuni.bg.bikeshop.models.orders.Order;
import softuni.bg.bikeshop.service.OrderService;

import java.time.LocalDate;
import java.util.List;

@Component
public class ArchivedOldOrdersScheduler {
    private final OrderService orderService;

    public ArchivedOldOrdersScheduler(OrderService orderService) {
        this.orderService = orderService;
    }
    @Scheduled(cron = "0 0 3 * * ?")
    public void deleteOldArchivedOrders() {
        LocalDate sixtyDaysAgo = LocalDate.now().minusDays(60);
        List<Order> oldArchivedOrders = orderService.findArchivedOrdersOlderThan(sixtyDaysAgo);

        for (Order order : oldArchivedOrders) {
            orderService.deleteOrder(order.getId());
        }
    }

}
