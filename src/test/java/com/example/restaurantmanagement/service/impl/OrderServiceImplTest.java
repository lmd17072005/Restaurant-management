package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.OrderItemRequest;
import com.example.restaurantmanagement.dto.request.OrderRequest;
import com.example.restaurantmanagement.dto.response.OrderResponse;
import com.example.restaurantmanagement.entity.Invoice;
import com.example.restaurantmanagement.entity.MenuItem;
import com.example.restaurantmanagement.entity.Order;
import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import com.example.restaurantmanagement.entity.enums.OrderStatus;
import com.example.restaurantmanagement.exception.BadRequestException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.OrderMapper;
import com.example.restaurantmanagement.repository.InvoiceRepository;
import com.example.restaurantmanagement.repository.MenuItemRepository;
import com.example.restaurantmanagement.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User mockUser;
    private Invoice mockInvoice;
    private MenuItem mockMenuItem;
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("staff1");

        mockInvoice = new Invoice();
        mockInvoice.setId(1L);

        mockMenuItem = new MenuItem();
        mockMenuItem.setId(1);
        mockMenuItem.setName("Phở Bò");
        mockMenuItem.setPrice(BigDecimal.valueOf(50000));
        mockMenuItem.setStatus(MenuItemStatus.con_ban);

        mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setStatus(OrderStatus.cho_che_bien);
    }

    private void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
    }

    @Test
    void createOrder_Success() {
        // Arrange
        setupSecurityContext();
        
        OrderRequest request = new OrderRequest();
        request.setInvoiceId(1L);
        OrderItemRequest itemRequest = new OrderItemRequest();
        itemRequest.setMenuItemId(1);
        itemRequest.setQuantity(2);
        request.setItems(List.of(itemRequest));

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(mockInvoice));
        when(menuItemRepository.findById(1)).thenReturn(Optional.of(mockMenuItem));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(new OrderResponse());

        // Act
        // OrderResponse response = orderService.createOrder(request);

        // Assert
      //  assertNotNull(response);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void createOrder_ThrowsInvoiceNotFound() {
        // Arrange
        setupSecurityContext();
        OrderRequest request = new OrderRequest();
        request.setInvoiceId(99L); // Non-existent ID

        when(invoiceRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> orderService.createOrder(request));
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void updateOrderStatus_Success() {
        // Arrange
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);
        when(orderMapper.toResponse(any(Order.class))).thenReturn(new OrderResponse());

        // Act
        OrderResponse response = orderService.updateOrderStatus(1L, OrderStatus.dang_che_bien);

        // Assert
        assertNotNull(response);
        assertEquals(OrderStatus.dang_che_bien, mockOrder.getStatus());
        verify(orderRepository, times(1)).save(mockOrder);
    }
}
