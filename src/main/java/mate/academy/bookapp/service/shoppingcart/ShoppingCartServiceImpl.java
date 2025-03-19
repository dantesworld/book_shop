package mate.academy.bookapp.service.shoppingcart;

import jakarta.transaction.Transactional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import mate.academy.bookapp.dto.cartitem.CartItemRequestDto;
import mate.academy.bookapp.dto.cartitem.UpdateCartItemRequestDto;
import mate.academy.bookapp.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookapp.exceptions.DataProcessingException;
import mate.academy.bookapp.exceptions.EntityNotFoundException;
import mate.academy.bookapp.mapper.CartItemMapper;
import mate.academy.bookapp.mapper.ShoppingCartMapper;
import mate.academy.bookapp.model.Book;
import mate.academy.bookapp.model.CartItem;
import mate.academy.bookapp.model.ShoppingCart;
import mate.academy.bookapp.model.User;
import mate.academy.bookapp.repository.book.BookRepository;
import mate.academy.bookapp.repository.cartitem.CartItemRepository;
import mate.academy.bookapp.repository.shoppingcart.ShoppingCartRepository;
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
    public ShoppingCartDto addCartItem(CartItemRequestDto requestDto, Long userId) {
        Book book = bookRepository.findById(requestDto.getBookId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Book not found with id: " + requestDto.getBookId())
                );

        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);

        checkBookInShoppingCart(shoppingCart.getCartItems(), book.getId());

        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);
        shoppingCart.getCartItems().add(cartItem);

        shoppingCartRepository.save(shoppingCart);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto getShoppingCartByUserId(Long userId) {
        return shoppingCartMapper.toDto(findShoppingCartByUserId(userId));
    }

    @Override
    public ShoppingCartDto updateCartItemById(UpdateCartItemRequestDto requestDto,
                                              Long id,
                                              Long userId) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);

        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(id, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find cart item with id: " + id)
                );

        cartItem.setQuantity(requestDto.getQuantity());

        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public void deleteCartItemById(Long cartItemId, Long userId) {
        ShoppingCart shoppingCart = findShoppingCartByUserId(userId);
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartId(
                cartItemId, shoppingCart.getId()).orElseThrow(
                    () -> new EntityNotFoundException(
                        "Can't find cartItem item with id: " + cartItemId)
                    );
        cartItemRepository.delete(cartItem);
        shoppingCart.getCartItems().remove(cartItem);
    }

    @Override
    public void createDefaultShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    private ShoppingCart findShoppingCartByUserId(Long userId) {
        return shoppingCartRepository.findShoppingCartByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shopping cart by userId: " + userId)
                );
    }

    private void checkBookInShoppingCart(Set<CartItem> cartItems, Long bookId) {
        boolean bookExists = cartItems.stream()
                .anyMatch(cartItem -> cartItem.getBook().getId().equals(bookId));

        if (bookExists) {
            throw new DataProcessingException(
                    "Book with id: " + bookId + " is already in the shopping cart!");
        }
    }
}
