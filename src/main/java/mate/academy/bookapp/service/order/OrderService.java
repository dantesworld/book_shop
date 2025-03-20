package mate.academy.bookapp.service.order;

import java.util.List;
import mate.academy.bookapp.dto.order.OrderItemResponseDto;
import mate.academy.bookapp.dto.order.OrderRequestDto;
import mate.academy.bookapp.dto.order.OrderResponseDto;
import mate.academy.bookapp.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.bookapp.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    OrderResponseDto createOrder(OrderRequestDto orderRequestDto, User user);

    Page<OrderResponseDto> getAllOrders(Pageable pageable, User user);

    List<OrderItemResponseDto> getOrderItemByOrderId(Long orderId, User user);

    OrderItemResponseDto getOrderItemByOrderItemId(Long orderItemId, User user, Long id);

    OrderResponseDto getOrderStatus(Long id, UpdateOrderStatusRequestDto requestDto);
}
