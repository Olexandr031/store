package com.example.store.service;

import com.example.store.dto.cart.AddToCartDTO;
import com.example.store.dto.cart.CartDTO;
import com.example.store.dto.cart.CartItemDTO;
import com.example.store.exception.CartItemNotExistException;
import com.example.store.model.Cart;
import com.example.store.model.Product;
import com.example.store.model.User;
import com.example.store.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    public CartService(){}

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public void addToCart(AddToCartDTO addToCartDTO, Product product, User user){
        Cart cart = new Cart(product, addToCartDTO.getQuantity(), user);
        cartRepository.save(cart);
    }


    public CartDTO listCartItems(User user) {
        List<Cart> cartList = cartRepository.findAllByUserOrderByCreatedDateDesc(user);
        List<CartItemDTO> cartItems = new ArrayList<>();
        for (Cart cart:cartList){
            CartItemDTO cartItemDTO = getDTOFromCart(cart);
            cartItems.add(cartItemDTO);
        }
        double totalCost = 0;
        for (CartItemDTO cartItemDTO :cartItems){
            totalCost += (cartItemDTO.getProduct().getPrice()* cartItemDTO.getQuantity());
        }
        return new CartDTO(cartItems,totalCost);
    }


    public static CartItemDTO getDTOFromCart(Cart cart) {
        return new CartItemDTO(cart);
    }


    public void updateCartItem(AddToCartDTO cartDTO, User user, Product product){
        Cart cart = cartRepository.getReferenceById(cartDTO.getId());
        cart.setQuantity(cartDTO.getQuantity());
        cart.setCreatedDate(new Date());
        cartRepository.save(cart);
    }

    public void deleteCartItem(int id,int userId) throws CartItemNotExistException {
        if (!cartRepository.existsById(id))
            throw new CartItemNotExistException("Cart id is invalid : " + id);
        cartRepository.deleteById(id);
    }

    public void deleteCartItems(int userId) {
        cartRepository.deleteAll();
    }


    public void deleteUserCartItems(User user) {
        cartRepository.deleteByUser(user);
    }
}
