package com.example.store.dto.checkout;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckoutItemDTO {
    private String productName;
    private int  quantity;
    private double price;
    private long productId;
    private int userId;
}
