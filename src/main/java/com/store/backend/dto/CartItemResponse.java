package com.store.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CartItemResponse {
    private Long cartId;
    private Long productId;
    private String productName;
    private String productDescription;
    private BigDecimal productPrice;
    private String productImage;
    private Integer quantity;
    private BigDecimal totalPrice;
    private Integer stock;
    private LocalDateTime createdAt;
    private Long categoryId;
    private String categoryName;

    public CartItemResponse() {}

    public CartItemResponse(Long cartId, Long productId, String productName, String productDescription, 
                           BigDecimal productPrice, String productImage, Integer quantity, 
                           Integer stock, LocalDateTime createdAt, Long categoryId, String categoryName) {
        this.cartId = cartId;
        this.productId = productId;
        this.productName = productName;
        this.productDescription = productDescription;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.quantity = quantity;
        this.totalPrice = productPrice.multiply(BigDecimal.valueOf(quantity));
        this.stock = stock;
        this.createdAt = createdAt;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public BigDecimal getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(BigDecimal productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        this.totalPrice = productPrice.multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
