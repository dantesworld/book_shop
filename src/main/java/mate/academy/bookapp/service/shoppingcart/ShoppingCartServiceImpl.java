package mate.academy.bookapp.service.shoppingcart;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.dto.cartitem.CartItemRequestDto;
import mate.academy.bookapp.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookapp.exceptions.EntityNotFoundException;
import mate.academy.bookapp.mapper.CartItemMapper;
import mate.academy.bookapp.mapper.ShoppingCartMapper;
import mate.academy.bookapp.model.CartItem;
import mate.academy.bookapp.model.ShoppingCart;
import mate.academy.bookapp.model.User;
import mate.academy.bookapp.repository.book.BookRepository;
import mate.academy.bookapp.repository.cartitem.CartItemRepository;
import mate.academy.bookapp.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto addCartItem(CartItemRequestDto requestDto,
                                       Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        CartItem cartItemExist = cartItemRepository.findByShoppingCartIdWhereBookId(user.getId(),
                requestDto.getBookId());
        if (cartItemExist != null) {
            return updateCartItemById(cartItemExist.getId(),
                    cartItemExist.getQuantity() + requestDto.getQuantity(),
                    authentication);
        }
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(shoppingCartRepository.findByUserId(user.getId()));
        cartItem.setBook(bookRepository.getReferenceById(requestDto.getBookId()));
        cartItemRepository.save(cartItem);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto find(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(user.getId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto updateCartItemById(Long id, int quantity,
                                              Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find cart item with id: "
                        + id));
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
        ShoppingCart shoppingCart = shoppingCartRepository
                .findByUserId(user.getId());
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteCartItemById(Long id) {
        cartItemRepository.deleteById(id);
    }

    @Override
    public void createDefaultShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private User getAuthenticatedUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }
}
