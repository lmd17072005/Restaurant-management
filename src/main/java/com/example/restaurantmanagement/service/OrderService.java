package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.OrderRequest;
import com.example.restaurantmanagement.dto.response.OrderResponse;
import com.example.restaurantmanagement.entity.enums.OrderStatus;

import java.util.List;

public interface OrderService {
    List<OrderResponse> getAllOrders();
    OrderResponse getOrderById(Long id);
    List<OrderResponse> getOrdersByInvoice(Long invoiceId);
    List<OrderResponse> getOrdersByStatus(OrderStatus status);
    OrderResponse createOrder(OrderRequest request);
    OrderResponse updateOrderStatus(Long id, OrderStatus status);
    void deleteOrder(Long id);
}

