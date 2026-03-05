package com.example.restaurantmanagement.service.impl;
import com.example.restaurantmanagement.dto.request.PaymentRequest;
import com.example.restaurantmanagement.dto.response.PaymentResponse;
import com.example.restaurantmanagement.entity.Order;
import com.example.restaurantmanagement.entity.Payment;
import com.example.restaurantmanagement.entity.enums.OrderStatus;
import com.example.restaurantmanagement.entity.enums.PaymentStatus;
import com.example.restaurantmanagement.exception.BadRequestException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.PaymentMapper;
import com.example.restaurantmanagement.repository.OrderRepository;
import com.example.restaurantmanagement.repository.PaymentRepository;
import com.example.restaurantmanagement.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;
    private final PaymentMapper paymentMapper;
    @Override public List<PaymentResponse> getAllPayments() { return paymentMapper.toResponseList(paymentRepository.findAll()); }
    @Override public PaymentResponse getPaymentById(Long id) { return paymentMapper.toResponse(paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id))); }
    @Override public PaymentResponse getPaymentByOrderId(Long orderId) { return paymentMapper.toResponse(paymentRepository.findByOrderId(orderId).orElseThrow(() -> new ResourceNotFoundException("Payment", "orderId", orderId))); }
    @Override @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {
        Order order = orderRepository.findById(request.getOrderId()).orElseThrow(() -> new ResourceNotFoundException("Order", "id", request.getOrderId()));
        paymentRepository.findByOrderId(request.getOrderId()).ifPresent(p -> { throw new BadRequestException("Payment already exists for order: " + request.getOrderId()); });
        if (order.getStatus() == OrderStatus.CANCELLED) throw new BadRequestException("Cannot pay for cancelled order");
        Payment payment = Payment.builder().order(order).amount(order.getTotalAmount()).paymentMethod(request.getPaymentMethod()).status(PaymentStatus.PENDING).build();
        return paymentMapper.toResponse(paymentRepository.save(payment));
    }
    @Override @Transactional
    public PaymentResponse completePayment(Long id) {
        Payment payment = paymentRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Payment", "id", id));
        if (payment.getStatus() == PaymentStatus.COMPLETED) throw new BadRequestException("Payment already completed");
        payment.setStatus(PaymentStatus.COMPLETED); payment.setPaymentTime(LocalDateTime.now());
        Order order = payment.getOrder(); order.setStatus(OrderStatus.COMPLETED); orderRepository.save(order);
        return paymentMapper.toResponse(paymentRepository.save(payment));
    }
}