package mate.academy.bookapp.mapper;

import mate.academy.bookapp.config.MapperConfig;
import mate.academy.bookapp.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookapp.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = {CartItemMapper.class})
public interface ShoppingCartMapper {
    @Mapping(source = "user.id", target = "userId")
    ShoppingCartDto toDto(ShoppingCart shoppingCart);
}
