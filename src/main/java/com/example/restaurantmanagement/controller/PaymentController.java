package com.example.restaurantmanagement.controller;
import com.example.restaurantmanagement.dto.request.PaymentRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.PaymentResponse;
import com.example.restaurantmanagement.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Payments", description = "Payment management API")
public class PaymentController {
    private final PaymentService paymentService;
    @GetMapping @PreAuthorize("hasAnyRole('ADMIN','STAFF')") @Operation(summary = "Get all payments")
    public ResponseEntity<ApiResponse<List<PaymentResponse>>> getAll() { return ResponseEntity.ok(ApiResponse.success(paymentService.getAllPayments())); }
    @GetMapping("/{id}") @Operation(summary = "Get payment by ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(paymentService.getPaymentById(id))); }
    @GetMapping("/order/{orderId}") @Operation(summary = "Get payment by order ID")
    public ResponseEntity<ApiResponse<PaymentResponse>> getByOrderId(@PathVariable Long orderId) { return ResponseEntity.ok(ApiResponse.success(paymentService.getPaymentByOrderId(orderId))); }
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','STAFF')") @Operation(summary = "Create payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> create(@Valid @RequestBody PaymentRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(paymentService.createPayment(request))); }
    @PatchMapping("/{id}/complete") @PreAuthorize("hasAnyRole('ADMIN','STAFF')") @Operation(summary = "Complete payment")
    public ResponseEntity<ApiResponse<PaymentResponse>> complete(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success("Payment completed", paymentService.completePayment(id))); }
}