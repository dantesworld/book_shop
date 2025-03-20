package mate.academy.bookapp.mapper;

import mate.academy.bookapp.config.MapperConfig;
import mate.academy.bookapp.dto.order.OrderResponseDto;
import mate.academy.bookapp.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "orderItems", target = "orderItems")
    OrderResponseDto toOrderResponseDto(Order order);
}
