package com.example.store.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddToCartDTO {
    private Integer id;
    private @NotNull Integer productId;
    private @NotNull Integer quantity;

    @Override
    public String toString() {
        return "AddToCartDTO{" +
                "id=" + id +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
