package com.example.store.service;


import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import com.example.store.dto.cart.CartDTO;
import com.example.store.dto.cart.CartItemDTO;
import com.example.store.dto.checkout.CheckoutItemDTO;
import com.example.store.exception.OrderNotFoundException;
import com.example.store.model.Order;
import com.example.store.model.OrderItem;
import com.example.store.model.User;
import com.example.store.repository.OrderItemRepository;
import com.example.store.repository.OrderRepository;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {
    @Autowired
    private CartService cartService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

//    @Value("${BASE_URL}")
    private String baseURL;

//    @Value("${STRIPE_SECRET_KEY}")
    private String apiKey;

    // create total price
    SessionCreateParams.LineItem.PriceData createPriceData(CheckoutItemDTO checkoutItemDTO) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmount((long)(checkoutItemDTO.getPrice()*100))
                .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                .setName(checkoutItemDTO.getProductName())
                                .build())
                .build();
    }

    // build each product in the stripe checkout page
    SessionCreateParams.LineItem createSessionLineItem(CheckoutItemDTO checkoutItemDTO) {
        return SessionCreateParams.LineItem.builder()
                .setPriceData(createPriceData(checkoutItemDTO))
                .setQuantity(Long.parseLong(String.valueOf(checkoutItemDTO.getQuantity())))
                .build();
    }

    // create session from list of checkout items
    public Session createSession(List<CheckoutItemDTO> checkoutItemDTOList) throws StripeException {

        String successURL = baseURL + "payment/success";
        String failedURL = baseURL + "payment/failed";

        Stripe.apiKey = apiKey;

        List<SessionCreateParams.LineItem> sessionItemsList = new ArrayList<>();

        for (CheckoutItemDTO checkoutItemDTO : checkoutItemDTOList) {
            sessionItemsList.add(createSessionLineItem(checkoutItemDTO));
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setCancelUrl(failedURL)
                .addAllLineItem(sessionItemsList)
                .setSuccessUrl(successURL)
                .build();
        return Session.create(params);
    }

    public void placeOrder(User user, String sessionId) {

        CartDTO cartDTO = cartService.listCartItems(user);

        List<CartItemDTO> cartItemDTOList = cartDTO.getCartItems();

        Order newOrder = new Order();
        newOrder.setCreatedDate(new Date());
        newOrder.setSessionId(sessionId);
        newOrder.setUser(user);
        newOrder.setTotalPrice(cartDTO.getTotalCost());
        orderRepository.save(newOrder);

        for (CartItemDTO cartItemDTO : cartItemDTOList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setCreatedDate(new Date());
            orderItem.setPrice(cartItemDTO.getProduct().getPrice());
            orderItem.setProduct(cartItemDTO.getProduct());
            orderItem.setQuantity(cartItemDTO.getQuantity());
            orderItem.setOrder(newOrder);
            orderItemRepository.save(orderItem);
        }

        cartService.deleteUserCartItems(user);
    }

    public List<Order> listOrders(User user) {
        return orderRepository.findAllByUserOrderByCreatedDateDesc(user);
    }


    public Order getOrder(Integer orderId) throws OrderNotFoundException {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent()) {
            return order.get();
        }
        throw new OrderNotFoundException("Order not found");
    }
}
