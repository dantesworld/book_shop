package mate.academy.bookapp.service.shoppingcart;

import mate.academy.bookapp.dto.cartitem.CartItemRequestDto;
import mate.academy.bookapp.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookapp.model.User;
import org.springframework.security.core.Authentication;

public interface ShoppingCartService {
    ShoppingCartDto addCartItem(CartItemRequestDto requestDto, Authentication authentication);

    ShoppingCartDto find(Authentication authentication);

    ShoppingCartDto updateCartItemById(Long id, int quantity, Authentication authentication);

    void deleteCartItemById(Long id);

    void createDefaultShoppingCart(User user);
}
