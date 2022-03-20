package com.spring.WebfluxTest.domain;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Item item;

    private int quantity;

    public void incrementQuantity() {
        quantity = quantity + 1;
    }

    public void decrementQuantity() {
        quantity = quantity - 1;
    }

    public boolean isOneQuantity() {
        return (this.quantity == 1) ? true : false;
    }

    public CartItem(Item item) {
        this.item = item;
    }
}
