package mate.academy.bookapp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.dto.order.OrderItemResponseDto;
import mate.academy.bookapp.dto.order.OrderRequestDto;
import mate.academy.bookapp.dto.order.OrderResponseDto;
import mate.academy.bookapp.dto.order.UpdateOrderStatusRequestDto;
import mate.academy.bookapp.model.User;
import mate.academy.bookapp.service.order.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management",
        description = "Endpoints for managing orders and order items")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Place an order",
            description = "Create a new order based on the user's shopping cart")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public OrderResponseDto createOrder(@RequestBody @Valid OrderRequestDto requestDto,
                                        Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(requestDto, user);
    }

    @Operation(summary = "Retrieve user's order history",
            description = "Get a paginated list of all orders for the user")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public Page<OrderResponseDto> getAllOrders(Pageable pageable,
                                               Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllOrders(pageable, user);
    }

    @Operation(summary = "Update order status",
            description = "Update the status of an order (Admin only)")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public OrderResponseDto updateOrderStatus(
            @RequestBody @Valid UpdateOrderStatusRequestDto requestDto,
                                              @PathVariable Long id) {
        return orderService.getOrderStatus(id, requestDto);
    }

    @Operation(summary = "Get all order items for an order",
            description = "Retrieve all order items for a specific order")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemResponseDto> getOrderItemsByOrderId(@PathVariable Long orderId,
                                                             Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemByOrderId(orderId, user);
    }

    @Operation(summary = "Get a specific order item",
            description = "Retrieve a specific order item within an order")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemResponseDto getOrderItemById(@PathVariable Long orderId,
                                                 @PathVariable Long itemId,
                                                 Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return orderService.getOrderItemByOrderItemId(itemId, user, orderId);
    }
}
