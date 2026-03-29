package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.PaymentRequest;
import com.example.restaurantmanagement.dto.response.PaymentResponse;
import com.example.restaurantmanagement.entity.Invoice;
import com.example.restaurantmanagement.entity.Payment;
import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import com.example.restaurantmanagement.entity.enums.PaymentMethod;
import com.example.restaurantmanagement.exception.BadRequestException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.PaymentMapper;
import com.example.restaurantmanagement.repository.InvoiceRepository;
import com.example.restaurantmanagement.repository.PaymentRepository;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private InvoiceRepository invoiceRepository;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private SecurityContext securityContext;
    @Mock
    private Authentication authentication;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private User mockUser;
    private Invoice mockInvoice;

    @BeforeEach
    void setUp() {
        mockUser = new User();
        mockUser.setId(1L);

        mockInvoice = new Invoice();
        mockInvoice.setId(1L);
        mockInvoice.setStatus(InvoiceStatus.chua_thanh_toan);
        mockInvoice.setTotalAmount(BigDecimal.valueOf(100000));
    }

    private void setupSecurityContext() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(mockUser);
    }

    @Test
    void createPayment_Success() {
        // Arrange
        setupSecurityContext();
        PaymentRequest request = new PaymentRequest();
        request.setInvoiceId(1L);
        request.setAmount(BigDecimal.valueOf(100000));
        request.setPaymentMethod(PaymentMethod.tien_mat);

        Payment savedPayment = new Payment();
        savedPayment.setId(1L);

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(mockInvoice));
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);
        when(paymentMapper.toResponse(any(Payment.class))).thenReturn(new PaymentResponse());

        // Act
        PaymentResponse response = paymentService.createPayment(request);

        // Assert
        assertNotNull(response);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void createPayment_ThrowsBadRequestWhenAlreadyPaid() {
        // Arrange
        setupSecurityContext();
        mockInvoice.setStatus(InvoiceStatus.da_thanh_toan);

        PaymentRequest request = new PaymentRequest();
        request.setInvoiceId(1L);
        request.setAmount(BigDecimal.valueOf(100000));

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(mockInvoice));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, 
            () -> paymentService.createPayment(request));
        assertEquals("Invoice already fully paid", exception.getMessage());
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void createPayment_ThrowsBadRequestWhenCancelled() {
        // Arrange
        setupSecurityContext();
        mockInvoice.setStatus(InvoiceStatus.da_huy);

        PaymentRequest request = new PaymentRequest();
        request.setInvoiceId(1L);
        request.setAmount(BigDecimal.valueOf(100000));

        when(invoiceRepository.findById(1L)).thenReturn(Optional.of(mockInvoice));

        // Act & Assert
        BadRequestException exception = assertThrows(BadRequestException.class, 
            () -> paymentService.createPayment(request));
        assertEquals("Cannot pay for cancelled invoice", exception.getMessage());
        verify(paymentRepository, never()).save(any(Payment.class));
    }
}
