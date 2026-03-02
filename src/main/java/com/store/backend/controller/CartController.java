package com.store.backend.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.store.backend.dto.CartItemResponse;
import com.store.backend.dto.CartRequest;
import com.store.backend.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addToCart(
            @RequestParam Long userId,
            @Valid @RequestBody CartRequest cartRequest) {
        CartItemResponse cartItem = cartService.addToCart(userId, cartRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItem);
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItems(@RequestParam Long userId) {
        List<CartItemResponse> cartItems = cartService.getCartItems(userId);
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("/update")
    public ResponseEntity<CartItemResponse> updateCartItem(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        CartItemResponse cartItem = cartService.updateCartItem(userId, productId, quantity);
        return ResponseEntity.ok(cartItem);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Map<String, String>> removeFromCart(
            @RequestParam Long userId,
            @RequestParam Long productId) {
        cartService.removeFromCart(userId, productId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Item removed from cart successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Map<String, String>> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cart cleared successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getCartItemCount(@RequestParam Long userId) {
        Long itemCount = cartService.getCartItemCount(userId);
        Map<String, Long> response = new HashMap<>();
        response.put("itemCount", itemCount);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getCartSummary(@RequestParam Long userId) {
        List<CartItemResponse> cartItems = cartService.getCartItems(userId);
        Long itemCount = cartService.getCartItemCount(userId);
        
        double totalAmount = cartItems.stream()
                .mapToDouble(item -> item.getTotalPrice().doubleValue())
                .sum();
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("itemCount", itemCount);
        summary.put("totalAmount", totalAmount);
        summary.put("items", cartItems);
        
        return ResponseEntity.ok(summary);
    }
}
