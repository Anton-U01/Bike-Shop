package softuni.bg.bikeshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.bg.bikeshop.models.User;
import softuni.bg.bikeshop.models.orders.Order;
import softuni.bg.bikeshop.models.orders.OrderStatus;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    Optional<Order> findByUserAndStatus(User user, OrderStatus orderStatus);
}
