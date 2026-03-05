package com.example.restaurantmanagement.controller;
import com.example.restaurantmanagement.dto.request.OrderRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.OrderResponse;
import com.example.restaurantmanagement.entity.enums.OrderStatus;
import com.example.restaurantmanagement.service.OrderService;
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
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Tag(name = "Orders", description = "Order management API")
public class OrderController {
    private final OrderService orderService;
    @GetMapping @Operation(summary = "Get all orders")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getAll() { return ResponseEntity.ok(ApiResponse.success(orderService.getAllOrders())); }
    @GetMapping("/{id}") @Operation(summary = "Get order by ID")
    public ResponseEntity<ApiResponse<OrderResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(orderService.getOrderById(id))); }
    @GetMapping("/status/{status}") @Operation(summary = "Get by status")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getByStatus(@PathVariable OrderStatus status) { return ResponseEntity.ok(ApiResponse.success(orderService.getOrdersByStatus(status))); }
    @GetMapping("/table/{tableId}") @Operation(summary = "Get by table")
    public ResponseEntity<ApiResponse<List<OrderResponse>>> getByTable(@PathVariable Long tableId) { return ResponseEntity.ok(ApiResponse.success(orderService.getOrdersByTable(tableId))); }
    @PostMapping @PreAuthorize("hasAnyRole('ADMIN','STAFF')") @Operation(summary = "Create order")
    public ResponseEntity<ApiResponse<OrderResponse>> create(@Valid @RequestBody OrderRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(orderService.createOrder(request))); }
    @PatchMapping("/{id}/status") @PreAuthorize("hasAnyRole('ADMIN','STAFF')") @Operation(summary = "Update order status")
    public ResponseEntity<ApiResponse<OrderResponse>> updateStatus(@PathVariable Long id, @RequestParam OrderStatus status) { return ResponseEntity.ok(ApiResponse.success("Status updated", orderService.updateOrderStatus(id, status))); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Delete order")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) { orderService.deleteOrder(id); return ResponseEntity.ok(ApiResponse.success("Deleted", null)); }
}