package com.store.backend.service;

import com.store.backend.dto.OrderRequest;
import com.store.backend.dto.OrderResponse;
import com.store.backend.entity.Order;
import com.store.backend.entity.OrderItem;
import com.store.backend.entity.Product;
import com.store.backend.entity.User;
import com.store.backend.repository.OrderRepository;
import com.store.backend.repository.OrderItemRepository;
import com.store.backend.repository.ProductRepository;
import com.store.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public OrderResponse createOrder(OrderRequest orderRequest) {
        // Validate user exists
        User user = userRepository.findById(orderRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate items and calculate totals
        List<OrderItem> orderItems = new ArrayList<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        Integer totalItems = 0;

        for (OrderRequest.OrderItemRequest itemRequest : orderRequest.getItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found: " + itemRequest.getProductId()));

            // Check stock availability
            if (product.getStock() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            // Create order item
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setSubtotal(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));

            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());
            totalItems += itemRequest.getQuantity();
        }

        // Generate random order number
        String orderNumber = generateOrderNumber();

        // Create order
        Order order = new Order();
        order.setUser(user);
        order.setOrderNumber(orderNumber);
        order.setTotalAmount(totalAmount);
        order.setTotalItems(totalItems);
        order.setStatus("PENDING");

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Save order items
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(savedOrder);
            orderItemRepository.save(orderItem);
        }

        return convertToOrderResponse(savedOrder);
    }

    public List<OrderResponse> getUserOrders(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderRepository.findByUserIdOrderByOrderDateDesc(userId);
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse getOrderByNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return convertToOrderResponse(order);
    }

    public List<OrderResponse> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(this::convertToOrderResponse)
                .collect(Collectors.toList());
    }

    public OrderResponse updateOrderStatus(String orderNumber, String status) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);
        Order updatedOrder = orderRepository.save(order);

        return convertToOrderResponse(updatedOrder);
    }

    private String generateOrderNumber() {
        Random random = new Random();
        int number = 100000 + random.nextInt(900000);
        return "ORD" + number;
    }

    private OrderResponse convertToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());
        response.setUserId(order.getUser().getId());
        response.setUserName(order.getUser().getName());
        response.setUserEmail(order.getUser().getEmail());
        response.setTotalAmount(order.getTotalAmount());
        response.setTotalItems(order.getTotalItems());
        response.setStatus(order.getStatus());
        response.setOrderDate(order.getOrderDate());

        // Convert order items
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        List<OrderResponse.OrderItemResponse> itemResponses = orderItems.stream()
                .map(this::convertToOrderItemResponse)
                .collect(Collectors.toList());
        response.setOrderItems(itemResponses);

        return response;
    }

    private OrderResponse.OrderItemResponse convertToOrderItemResponse(OrderItem orderItem) {
        OrderResponse.OrderItemResponse response = new OrderResponse.OrderItemResponse();
        response.setId(orderItem.getId());
        response.setProductId(orderItem.getProduct().getId());
        response.setProductName(orderItem.getProduct().getName());
        response.setProductDescription(orderItem.getProduct().getDescription());
        response.setPrice(orderItem.getPrice());
        response.setQuantity(orderItem.getQuantity());
        response.setSubtotal(orderItem.getSubtotal());
        return response;
    }
}
