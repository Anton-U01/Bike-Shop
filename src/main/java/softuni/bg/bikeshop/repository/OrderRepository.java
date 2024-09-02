package softuni.bg.bikeshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.orders.Order;
import softuni.bg.bikeshop.models.orders.OrderStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByUserAndStatus(User user, OrderStatus orderStatus);

    List<Order> findByStatusIn(List<OrderStatus> statuses);

    List<Order> findByStatusAndOrderDateBefore(OrderStatus status, LocalDate date);
}
