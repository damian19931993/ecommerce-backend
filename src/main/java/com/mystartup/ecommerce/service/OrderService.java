package com.mystartup.ecommerce.service;

import com.mystartup.ecommerce.entity.Order;
import com.mystartup.ecommerce.entity.OrderItem;
import com.mystartup.ecommerce.entity.Cart;
import com.mystartup.ecommerce.entity.CartItem;
import com.mystartup.ecommerce.repository.OrderRepository;
import com.mystartup.ecommerce.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private CartService cartService;

    @Transactional
    public Order createOrderFromCart(Long userId, String shippingAddress, String shippingCity, String shippingCountry, String paymentMethod) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setTotalAmount(cart.getTotalAmount());
        order.setStatus("Pending");
        order.setCreatedAt(LocalDateTime.now());
        order.setShippingAddress(shippingAddress);
        order.setShippingCity(shippingCity);
        order.setShippingCountry(shippingCountry);
        order.setPaymentMethod(paymentMethod);
        order = orderRepository.save(order);

        List<CartItem> cartItems = cart.getCartItems();
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice());
            orderItemRepository.save(orderItem);
        }

        cartService.clearCart(userId);

        return order;
    }
}
