package com.store.backend.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.store.backend.dto.CartItemResponse;
import com.store.backend.dto.CartRequest;
import com.store.backend.entity.Cart;
import com.store.backend.entity.Product;
import com.store.backend.entity.User;
import com.store.backend.exception.ProductNotFoundException;
import com.store.backend.exception.UserNotFoundException;
import com.store.backend.repository.CartRepository;
import com.store.backend.repository.ProductRepository;
import com.store.backend.repository.UserRepository;

import jakarta.persistence.PrePersist;

@Service
@Transactional
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    public CartItemResponse addToCart(Long userId, CartRequest cartRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        
        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + cartRequest.getProductId()));

        // Check if product is active
        if (!product.getIsActive()) {
            throw new RuntimeException("Product is not available: " + product.getName());
        }

        // Check stock availability
        if (product.getStock() < cartRequest.getQuantity()) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStock() + ", Requested: " + cartRequest.getQuantity());
        }

        // Check if item already exists in cart
        Cart existingCartItem = cartRepository.findByUserIdAndProductId(userId, cartRequest.getProductId()).orElse(null);
        
        if (existingCartItem != null) {
            // Update quantity
            int newQuantity = existingCartItem.getQuantity() + cartRequest.getQuantity();
            
            // Check stock again for updated quantity
            if (product.getStock() < newQuantity) {
                throw new RuntimeException("Insufficient stock for updated quantity. Available: " + product.getStock() + ", Requested: " + newQuantity);
            }
            
            existingCartItem.setQuantity(newQuantity);
            existingCartItem.setUpdatedAt(java.time.LocalDateTime.now());
            Cart savedCart = cartRepository.save(existingCartItem);
            return convertToCartItemResponse(savedCart);
        } else {
            // Create new cart item
            Cart cartItem = new Cart(user, product, cartRequest.getQuantity());
            Cart savedCart = cartRepository.save(cartItem);
            return convertToCartItemResponse(savedCart);
        }
    }

    public List<CartItemResponse> getCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        List<Cart> cartItems = cartRepository.findByUserId(userId);
        
        return cartItems.stream()
                .map(this::convertToCartItemResponse)
                .collect(Collectors.toList());
    }

    public CartItemResponse updateCartItem(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        Product product = cartItem.getProduct();
        
        // Check stock availability
        if (product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock. Available: " + product.getStock() + ", Requested: " + quantity);
        }

        cartItem.setQuantity(quantity);
        cartItem.setUpdatedAt(java.time.LocalDateTime.now());
        Cart savedCart = cartRepository.save(cartItem);
        
        return convertToCartItemResponse(savedCart);
    }

    public void removeFromCart(Long userId, Long productId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        Cart cartItem = cartRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new RuntimeException("Item not found in cart"));

        cartRepository.delete(cartItem);
    }

    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        cartRepository.deleteByUserId(userId);
    }

    public Long getCartItemCount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        return cartRepository.getTotalItemsByUserId(userId);
    }

    private CartItemResponse convertToCartItemResponse(Cart cart) {
        Product product = cart.getProduct();
        String productImage = product.getImages() != null && !product.getImages().isEmpty() 
                ? product.getImages().get(0) 
                : null;
        
        Long categoryId = product.getCategory() != null ? product.getCategory().getId() : null;
        String categoryName = product.getCategory() != null ? product.getCategory().getName() : null;

        return new CartItemResponse(
                cart.getId(),
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                productImage,
                cart.getQuantity(),
                product.getStock(),
                cart.getCreatedAt(),
                categoryId,
                categoryName
        );
    }
}
