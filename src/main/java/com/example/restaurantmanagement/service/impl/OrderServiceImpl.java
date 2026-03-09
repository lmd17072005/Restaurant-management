package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.OrderItemRequest;
import com.example.restaurantmanagement.dto.request.OrderRequest;
import com.example.restaurantmanagement.dto.response.OrderResponse;
import com.example.restaurantmanagement.entity.*;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import com.example.restaurantmanagement.entity.enums.OrderStatus;
import com.example.restaurantmanagement.exception.BadRequestException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.OrderMapper;
import com.example.restaurantmanagement.repository.InvoiceRepository;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.repository.OrderRepository;
import com.example.restaurantmanagement.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final InvoiceRepository invoiceRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderResponse> getAllOrders() {
        return orderMapper.toResponseList(orderRepository.findAll());
    }

    @Override
    public OrderResponse getOrderById(Long id) {
        return orderMapper.toResponse(orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id)));
    }

    @Override
    public List<OrderResponse> getOrdersByInvoice(Long invoiceId) {
        return orderMapper.toResponseList(orderRepository.findByInvoiceId(invoiceId));
    }

    @Override
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        return orderMapper.toResponseList(orderRepository.findByStatus(status));
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", request.getInvoiceId()));

        User currentUser = getCurrentUser();

        Order order = Order.builder()
                .invoice(invoice)
                .createdBy(currentUser)
                .status(OrderStatus.cho_che_bien)
                .orderItems(new ArrayList<>())
                .build();

        for (OrderItemRequest itemReq : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", itemReq.getMenuItemId()));

            if (menuItem.getStatus() != MenuItemStatus.con_ban) {
                throw new BadRequestException("Menu item not available: " + menuItem.getName());
            }

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .menuItem(menuItem)
                    .quantity(itemReq.getQuantity())
                    .unitPrice(menuItem.getPrice())
                    .note(itemReq.getNote())
                    .build();

            order.getOrderItems().add(orderItem);
        }

        // Invoice subtotal/total recalculated by PostgreSQL trigger fn_ctdh_tinh_lai_hd
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        order.setStatus(status);
        return orderMapper.toResponse(orderRepository.save(order));
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order", "id", id);
        }
        orderRepository.deleteById(id);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }
}

