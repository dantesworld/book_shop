package mate.academy.bookapp.service.order;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.dto.order.OrderItemResponseDto;
import mate.academy.bookapp.dto.order.OrderRequestDto;
import mate.academy.bookapp.dto.order.OrderResponseDto;
import mate.academy.bookapp.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.bookapp.exceptions.EntityNotFoundException;
import mate.academy.bookapp.mapper.OrderItemMapper;
import mate.academy.bookapp.mapper.OrderMapper;
import mate.academy.bookapp.model.Book;
import mate.academy.bookapp.model.CartItem;
import mate.academy.bookapp.model.Order;
import mate.academy.bookapp.model.OrderItem;
import mate.academy.bookapp.model.ShoppingCart;
import mate.academy.bookapp.model.User;
import mate.academy.bookapp.repository.order.OrderItemRepository;
import mate.academy.bookapp.repository.order.OrderRepository;
import mate.academy.bookapp.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public OrderResponseDto createOrder(OrderRequestDto orderRequestDto, User user) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Shopping cart not found for user: "
                        + user.getId()));

        if (shoppingCart.getCartItems().isEmpty()) {
            throw new IllegalStateException("Cannot create an order from an empty shopping cart");
        }

        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(orderRequestDto.getShippingAddress());
        order.setOrderDate(LocalDateTime.now());

        BigDecimal total = BigDecimal.ZERO;
        Set<OrderItem> orderItems = new HashSet<>();
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            Book book = cartItem.getBook();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setBook(book);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(book.getPrice());
            orderItems.add(orderItem);

            BigDecimal itemTotal = book.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(itemTotal);
        }

        order.setTotal(total);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);

        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toOrderResponseDto(savedOrder);
    }

    @Override
    public Page<OrderResponseDto> getAllOrders(Pageable pageable, User user) {
        Page<Order> orders = orderRepository.findAllByUserId(user.getId(), pageable);
        return orders.map(orderMapper::toOrderResponseDto);
    }

    @Override
    public List<OrderItemResponseDto> getOrderItemByOrderId(Long orderId, User user) {
        Order order = orderRepository.findByIdAndUserId(orderId, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: "
                        + orderId));

        Set<OrderItem> orderItems = order.getOrderItems();

        return orderItems.stream()
                .map(orderItemMapper::toOrderItemResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemResponseDto getOrderItemByOrderItemId(Long orderItemId, User user, Long id) {
        OrderItem orderItem = orderItemRepository
                .findByIdAndOrderIdAndOrderUserId(orderItemId, id, user.getId())
                .orElseThrow(() -> new EntityNotFoundException("Order item not found with id: "
                        + orderItemId));

        return orderItemMapper.toOrderItemResponseDto(orderItem);
    }

    @Override
    public OrderResponseDto getOrderStatus(Long id, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        order.setStatus(requestDto.getStatus());
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponseDto(updatedOrder);
    }
}
