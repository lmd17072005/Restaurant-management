package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.PaymentRequest;
import com.example.restaurantmanagement.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    List<PaymentResponse> getAllPayments();
    PaymentResponse getPaymentById(Long id);
    PaymentResponse getPaymentByOrderId(Long orderId);
    PaymentResponse createPayment(PaymentRequest request);
    PaymentResponse completePayment(Long id);
}

