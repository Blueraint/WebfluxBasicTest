package com.spring.WebfluxTest.controller;

import com.spring.WebfluxTest.domain.Cart;
import com.spring.WebfluxTest.repository.CartReactiveRepository;
import com.spring.WebfluxTest.repository.ItemReactiveRepository;
import com.spring.WebfluxTest.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final ItemReactiveRepository itemReactiveRepository;
    private final CartReactiveRepository cartReactiveRepository;
    private final CartService cartService;

    @GetMapping("/")
    Mono<Rendering> index() {
        return Mono.just(Rendering.view("index.html")
                .modelAttribute("items",itemReactiveRepository.findAll())
                .modelAttribute("cart",cartReactiveRepository.findById("MyCart").defaultIfEmpty(new Cart("MyCart")))
                .build()
        );
    }

    @PostMapping("/add/{id}")
    Mono<String> addCart(@PathVariable String id) {
        return cartService.addToCart("MyCart", id).thenReturn("redirect:/");
    }

    @DeleteMapping("deleteCartItem/{id}")
    Mono<String> decrementToCartCount(@PathVariable String id) {
        return cartService.decrementToCartCount("MyCart", id).thenReturn("redirect:/");
    }

    @DeleteMapping("deleteCartAll/{id}")
    Mono<String> deleteToCartAll(@PathVariable String id) {
        return cartService.deleteToCartAll("MyCart", id).thenReturn("redirect:/");
    }

    @DeleteMapping("/deleteItem/{id}")
    Mono<String> deleteItem(@PathVariable String id) {
        return this.itemReactiveRepository.deleteById(id).thenReturn("redirect:/");
    }
}
