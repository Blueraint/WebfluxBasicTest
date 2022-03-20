package com.spring.WebfluxTest.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    private String id;

    private List<CartItem> cardItemList = new ArrayList<>();

    public void removeItem(CartItem cartItem) {
        this.cardItemList.remove(cartItem);
    }

    public Cart(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id='" + id + '\'' +
                ", cardItemList=" + cardItemList.toString() +
                '}';
    }
}
