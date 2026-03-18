package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.PaymentRequest;
import com.example.restaurantmanagement.dto.response.PaymentResponse;
import com.example.restaurantmanagement.entity.Invoice;
import com.example.restaurantmanagement.entity.Payment;
import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import com.example.restaurantmanagement.exception.BadRequestException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.PaymentMapper;
import com.example.restaurantmanagement.repository.InvoiceRepository;
import com.example.restaurantmanagement.repository.PaymentRepository;
import com.example.restaurantmanagement.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMapper paymentMapper;

    @Override
    @Transactional
    public List<PaymentResponse> getAllPayments() {
        return paymentMapper.toResponseList(paymentRepository.findAll());
    }

    @Override
    @Transactional
    public PaymentResponse getPaymentById(Long id) {
        return paymentMapper.toResponse(paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id)));
    }

    @Override
    @Transactional
    public List<PaymentResponse> getPaymentsByInvoice(Long invoiceId) {
        return paymentMapper.toResponseList(paymentRepository.findByInvoiceId(invoiceId));
    }

    @Override
    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", request.getInvoiceId()));

        if (invoice.getStatus() == InvoiceStatus.da_thanh_toan) {
            throw new BadRequestException("Invoice already fully paid");
        }
        if (invoice.getStatus() == InvoiceStatus.da_huy) {
            throw new BadRequestException("Cannot pay for cancelled invoice");
        }

        User currentUser = getCurrentUser();

        Payment payment = Payment.builder()
                .invoice(invoice)
                .amount(request.getAmount())
                .paymentMethod(request.getPaymentMethod())
                .processedBy(currentUser)
                .build();

        // Invoice status update is handled by PostgreSQL trigger fn_thanh_toan_cap_nhat_trang_thai
        return paymentMapper.toResponse(paymentRepository.save(payment));
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }
}

