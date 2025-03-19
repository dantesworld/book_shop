package mate.academy.bookapp.dto.shoppingcart;

import java.util.List;
import lombok.Data;
import mate.academy.bookapp.dto.cartitem.CartItemDto;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private List<CartItemDto> cartItems;
}
