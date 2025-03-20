package mate.academy.bookapp.repository.order;

import java.util.Optional;
import mate.academy.bookapp.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @EntityGraph(attributePaths = "orderItems")
    Page<Order> findAllByUserId(Long id, Pageable pageable);

    @EntityGraph(attributePaths = "orderItems")
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}
