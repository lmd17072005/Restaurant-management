package com.example.restaurantmanagement.service;

import com.example.restaurantmanagement.dto.request.InvoiceRequest;
import com.example.restaurantmanagement.dto.response.InvoiceResponse;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;

import java.util.List;

public interface InvoiceService {
    List<InvoiceResponse> getAllInvoices();
    InvoiceResponse getInvoiceById(Long id);
    List<InvoiceResponse> getInvoicesByStatus(InvoiceStatus status);
    InvoiceResponse createInvoice(InvoiceRequest request);
    InvoiceResponse updateDiscount(Long id, InvoiceRequest request);
    InvoiceResponse cancelInvoice(Long id);
}

