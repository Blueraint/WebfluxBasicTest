package com.spring.WebfluxTest.repository;

import com.spring.WebfluxTest.domain.Item;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ItemReactiveRepository extends ReactiveCrudRepository<Item, String>, ReactiveQueryByExampleExecutor<Item> {
    // Single Item
    Mono<Item> findByName(String name);

    // Multiple Items
    Flux<Item> findByNameContaining(String itemName);


}
