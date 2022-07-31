package com.example.store.dto.cart;

import com.example.store.model.Cart;
import com.example.store.model.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItemDTO {
    private Integer id;
    private @NotNull Integer quantity;
    private @NotNull
    Product product;

    public CartItemDTO(Cart cart) { // from IDEA
    }

    @Override
    public String toString() {
        return "CartItemDTO{" +
                "id=" + id +
                ", quantity=" + quantity +
                ", product=" + product +
                '}';
    }
}
