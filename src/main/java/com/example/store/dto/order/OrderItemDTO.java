package com.example.store.dto.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class OrderItemDTO {
    private @NotNull double price;
    private @NotNull int quantity;
    private @NotNull int orderId;
    private @NotNull int productId;
}
