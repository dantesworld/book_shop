package mate.academy.bookapp.service.shoppingcart;

import mate.academy.bookapp.dto.cartitem.CartItemRequestDto;
import mate.academy.bookapp.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.bookapp.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookapp.model.User;

public interface ShoppingCartService {
    ShoppingCartDto addCartItem(CartItemRequestDto requestDto, Long userId);

    ShoppingCartDto getShoppingCartByUserId(Long userId);

    ShoppingCartDto updateCartItemById(UpdateCartItemRequestDto requestDto,
                                       Long id,
                                       Long userId);

    void deleteCartItemById(Long cartItemId, Long userId);

    void createDefaultShoppingCart(User user);
}
