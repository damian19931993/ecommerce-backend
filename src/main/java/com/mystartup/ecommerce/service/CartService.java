package com.mystartup.ecommerce.service;

import com.mystartup.ecommerce.entity.Cart;
import com.mystartup.ecommerce.entity.CartItem;
import com.mystartup.ecommerce.entity.Product;
import com.mystartup.ecommerce.repository.CartItemRepository;
import com.mystartup.ecommerce.repository.CartRepository;
import com.mystartup.ecommerce.repository.ProductRepository;
import com.mystartup.ecommerce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    public Cart addToCart(Long userId, Long productId, int quantity) {
        Optional<Cart> optionalCart = cartRepository.findByUserId(userId);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        Cart cart;
        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            cart = new Cart();
            cart.setUser(userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")));
            cart.setStatus("ACTIVE"); // Set the default status
            cartRepository.save(cart);
        }

        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        cartItem.setCart(cart);

        cart.getCartItems().add(cartItem);
        cartItemRepository.save(cartItem);

        return cartRepository.save(cart);
    }

    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId).orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    public void removeFromCart(Long cartItemId) {
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        cartItemRepository.delete(cartItem);
    }
}

