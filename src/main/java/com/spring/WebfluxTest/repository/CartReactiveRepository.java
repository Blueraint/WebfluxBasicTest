package com.spring.WebfluxTest.repository;

import com.spring.WebfluxTest.domain.Cart;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CartReactiveRepository extends ReactiveCrudRepository<Cart, String> {
}
