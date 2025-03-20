package mate.academy.bookapp.repository.order;

import java.util.Optional;
import mate.academy.bookapp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    Optional<OrderItem> findByIdAndOrderIdAndOrderUserId(Long itemId,
                                                         Long orderId,
                                                         Long userId);
}
