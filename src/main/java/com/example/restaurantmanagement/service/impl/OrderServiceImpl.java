package com.example.restaurantmanagement.service.impl;
import com.example.restaurantmanagement.dto.request.OrderItemRequest;
import com.example.restaurantmanagement.dto.request.OrderRequest;
import com.example.restaurantmanagement.dto.response.OrderResponse;
import com.example.restaurantmanagement.entity.*;
import com.example.restaurantmanagement.entity.enums.OrderStatus;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import com.example.restaurantmanagement.exception.BadRequestException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.OrderMapper;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.repository.OrderRepository;
import com.example.restaurantmanagement.repository.RestaurantTableRepository;
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
    private final RestaurantTableRepository tableRepository;
    private final MenuItemRepository menuItemRepository;
    private final OrderMapper orderMapper;
    @Override public List<OrderResponse> getAllOrders() { return orderMapper.toResponseList(orderRepository.findAll()); }
    @Override public OrderResponse getOrderById(Long id) { return orderMapper.toResponse(orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id))); }
    @Override public List<OrderResponse> getOrdersByStatus(OrderStatus status) { return orderMapper.toResponseList(orderRepository.findByStatus(status)); }
    @Override public List<OrderResponse> getOrdersByTable(Long tableId) { return orderMapper.toResponseList(orderRepository.findByTableId(tableId)); }
    @Override @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        RestaurantTable table = tableRepository.findById(request.getTableId()).orElseThrow(() -> new ResourceNotFoundException("Table", "id", request.getTableId()));
        User currentUser = null;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) currentUser = (User) auth.getPrincipal();
        Order order = Order.builder().table(table).user(currentUser).status(OrderStatus.PENDING).note(request.getNote()).orderItems(new ArrayList<>()).build();
        for (OrderItemRequest itemReq : request.getItems()) {
            MenuItem menuItem = menuItemRepository.findById(itemReq.getMenuItemId()).orElseThrow(() -> new ResourceNotFoundException("MenuItem", "id", itemReq.getMenuItemId()));
            if (!menuItem.getIsAvailable()) throw new BadRequestException("Menu item not available: " + menuItem.getName());
            OrderItem orderItem = OrderItem.builder().order(order).menuItem(menuItem).quantity(itemReq.getQuantity()).unitPrice(menuItem.getPrice()).note(itemReq.getNote()).build();
            order.getOrderItems().add(orderItem);
        }
        order.recalculateTotal();
        table.setStatus(TableStatus.OCCUPIED); tableRepository.save(table);
        return orderMapper.toResponse(orderRepository.save(order));
    }
    @Override @Transactional
    public OrderResponse updateOrderStatus(Long id, OrderStatus status) {
        Order order = orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Order", "id", id));
        order.setStatus(status);
        if (status == OrderStatus.COMPLETED || status == OrderStatus.CANCELLED) {
            RestaurantTable table = order.getTable();
            List<Order> active = new ArrayList<>(orderRepository.findByTableIdAndStatus(table.getId(), OrderStatus.PENDING));
            active.addAll(orderRepository.findByTableIdAndStatus(table.getId(), OrderStatus.PREPARING));
            active.addAll(orderRepository.findByTableIdAndStatus(table.getId(), OrderStatus.SERVED));
            active.removeIf(o -> o.getId().equals(id));
            if (active.isEmpty()) { table.setStatus(TableStatus.AVAILABLE); tableRepository.save(table); }
        }
        return orderMapper.toResponse(orderRepository.save(order));
    }
    @Override @Transactional
    public void deleteOrder(Long id) {
        if (!orderRepository.existsById(id)) throw new ResourceNotFoundException("Order", "id", id);
        orderRepository.deleteById(id);
    }
}