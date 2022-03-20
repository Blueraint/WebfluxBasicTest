package com.spring.WebfluxTest.service;

import com.spring.WebfluxTest.domain.Cart;
import com.spring.WebfluxTest.domain.CartItem;
import com.spring.WebfluxTest.domain.Item;
import com.spring.WebfluxTest.repository.CartReactiveRepository;
import com.spring.WebfluxTest.repository.ItemReactiveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.contains;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final ItemReactiveRepository itemReactiveRepository;
    private final CartReactiveRepository cartReactiveRepository;

    // Find Item
    @Override
    public Flux<Item> itemSearchByName(String name, String description, boolean isSuit) {
        Item item = new Item(name, description, 0L);

        ExampleMatcher matcher = (isSuit ? ExampleMatcher.matchingAll().withIgnorePaths("price")
                : ExampleMatcher.matching()
                    .withMatcher("name", contains())
                    .withMatcher("description", contains())
                    .withIgnorePaths("price")
        );

        Example<Item> itemExample = Example.of(item, matcher);

        return itemReactiveRepository.findAll(itemExample);
    }

    @Override
    public Mono<Cart> decrementToCartCount(String cartId, String id) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(
                        cart ->
                            cart.getCardItemList() // get Cart's Item List
                            .stream()
                            .filter(cartItem -> cartItem.getItem().getId().equals(id))
                            .findAny() // If "Cart's item id" is equal to "request's id"
                            .map(cartItem -> {
                                if(cartItem.isOneQuantity()) // If "Cart's item id" is the only one, delete cartItem forever(remove it!)
                                    cart.removeItem(cartItem);
                                else // If "Cart's item id" count is over two, decrease these size of quantity
                                    cartItem.decrementQuantity();
                                return Mono.just(cart); // return Cart Mono(consumer)
                            })
                            .orElseGet(
                                    () -> { // Null(Empty) return them
                                        return Mono.empty();
                                }
                            )
                )
                .flatMap(cart -> cartReactiveRepository.save(cart));

    }

    @Override
    public Mono<Cart> deleteToCartAll(String cartId, String id) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(
                        cart -> cart.getCardItemList()
                                .stream()
                                .filter(cartItem -> cartItem.getItem().getId().equals(id))
                                .findAny() // If "Cart's item id" is equal to "request's id"
                                .map(cartItem -> {
                                    cart.removeItem(cartItem); // delete cartItem forever(remove it!)
                                    return Mono.just(cart);
                                })
                                .orElseGet(() -> {
                                    return Mono.empty();
                                })
                ).flatMap(cart -> cartReactiveRepository.save(cart));
    }

    @Override
    public Mono<Cart> addToCart(String cartId, String id) {
        return cartReactiveRepository.findById(cartId)
                .defaultIfEmpty(new Cart(cartId))
                .flatMap(
                        cart -> cart.getCardItemList()
                                .stream()
                                .filter(cartItem -> cartItem.getItem().getId().equals(id))
                                .findAny() // If "Cart's item id" is equal to "request's id"
                                .map(cartItem -> {
                                    cartItem.incrementQuantity(); // increase these size of quantity
                                    return Mono.just(cart);
                                }).orElseGet( // Empty Cart List(CartItem)
                                        () -> itemReactiveRepository
                                                .findById(id)
                                                .map(item -> new CartItem(item)) // New Item that will be inserted by Customer's Cart!
                                                .doOnNext(cartItem -> cart.getCardItemList().add(cartItem)) // Insert Them
                                                .map(cartItem -> cart) // return cart
                                )
                ).flatMap(cart -> cartReactiveRepository.save(cart));
    }
}
