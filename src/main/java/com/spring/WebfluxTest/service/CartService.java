package com.spring.WebfluxTest.service;

import com.spring.WebfluxTest.domain.Cart;
import com.spring.WebfluxTest.domain.Item;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CartService {
    public Flux<Item> itemSearchByName(String name, String description, boolean isSuit);

    public Mono<Cart> decrementToCartCount(String cartId, String id);

    public Mono<Cart> deleteToCartAll(String cartId, String id);

    public Mono<Cart> addToCart(String cartId, String id);
}
