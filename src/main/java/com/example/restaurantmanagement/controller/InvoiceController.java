package com.example.restaurantmanagement.controller;

import com.example.restaurantmanagement.dto.request.InvoiceRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.InvoiceResponse;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import com.example.restaurantmanagement.service.InvoiceService;
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
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Tag(name = "Invoices", description = "Invoice management API")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Get all invoices")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getAllInvoices()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Get invoice by ID")
    public ResponseEntity<ApiResponse<InvoiceResponse>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getInvoiceById(id)));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Get invoices by status")
    public ResponseEntity<ApiResponse<List<InvoiceResponse>>> getByStatus(@PathVariable InvoiceStatus status) {
        return ResponseEntity.ok(ApiResponse.success(invoiceService.getInvoicesByStatus(status)));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Create invoice (open a new bill)")
    public ResponseEntity<ApiResponse<InvoiceResponse>> create(@Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(invoiceService.createInvoice(request)));
    }

    @PutMapping("/{id}/discount")
    @PreAuthorize("hasAnyRole('QUAN_LY','NHAN_VIEN')")
    @Operation(summary = "Update invoice discount")
    public ResponseEntity<ApiResponse<InvoiceResponse>> updateDiscount(@PathVariable Long id, @Valid @RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(ApiResponse.success("Discount updated", invoiceService.updateDiscount(id, request)));
    }

    @PatchMapping("/{id}/cancel")
    @PreAuthorize("hasRole('QUAN_LY')")
    @Operation(summary = "Cancel invoice")
    public ResponseEntity<ApiResponse<InvoiceResponse>> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success("Invoice cancelled", invoiceService.cancelInvoice(id)));
    }
}

