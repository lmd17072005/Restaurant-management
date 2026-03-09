package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.PaymentRequest;
import com.example.restaurantmanagement.dto.response.PaymentResponse;

import java.util.List;

public interface PaymentService {
    List<PaymentResponse> getAllPayments();
    PaymentResponse getPaymentById(Long id);
    List<PaymentResponse> getPaymentsByInvoice(Long invoiceId);
    PaymentResponse createPayment(PaymentRequest request);
}

