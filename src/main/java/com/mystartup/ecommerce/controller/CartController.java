package com.mystartup.ecommerce.controller;

import com.mystartup.ecommerce.dto.CartItemRequest;
import com.mystartup.ecommerce.entity.Cart;
import com.mystartup.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/{userId}/add")
    public Cart addToCart(
            @PathVariable Long userId,
            @RequestBody CartItemRequest cartItemRequest) {
        return cartService.addToCart(userId, cartItemRequest.getProductId(), cartItemRequest.getQuantity());
    }

    @GetMapping("/{userId}")
    public Cart getCartByUserId(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    @DeleteMapping("/remove/{cartItemId}")
    public void removeFromCart(@PathVariable Long cartItemId) {
        cartService.removeFromCart(cartItemId);
    }
}
