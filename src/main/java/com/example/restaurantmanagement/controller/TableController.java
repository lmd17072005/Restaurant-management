package com.example.restaurantmanagement.controller;
import com.example.restaurantmanagement.dto.request.TableRequest;
import com.example.restaurantmanagement.dto.response.ApiResponse;
import com.example.restaurantmanagement.dto.response.TableResponse;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import com.example.restaurantmanagement.service.TableService;
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
@RequestMapping("/api/tables")
@RequiredArgsConstructor
@Tag(name = "Tables", description = "Table management API")
public class TableController {
    private final TableService tableService;
    @GetMapping @Operation(summary = "Get all tables")
    public ResponseEntity<ApiResponse<List<TableResponse>>> getAll() { return ResponseEntity.ok(ApiResponse.success(tableService.getAllTables())); }
    @GetMapping("/{id}") @Operation(summary = "Get table by ID")
    public ResponseEntity<ApiResponse<TableResponse>> getById(@PathVariable Long id) { return ResponseEntity.ok(ApiResponse.success(tableService.getTableById(id))); }
    @GetMapping("/status/{status}") @Operation(summary = "Get tables by status")
    public ResponseEntity<ApiResponse<List<TableResponse>>> getByStatus(@PathVariable TableStatus status) { return ResponseEntity.ok(ApiResponse.success(tableService.getTablesByStatus(status))); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Create table")
    public ResponseEntity<ApiResponse<TableResponse>> create(@Valid @RequestBody TableRequest request) { return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(tableService.createTable(request))); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Update table")
    public ResponseEntity<ApiResponse<TableResponse>> update(@PathVariable Long id, @Valid @RequestBody TableRequest request) { return ResponseEntity.ok(ApiResponse.success("Updated", tableService.updateTable(id, request))); }
    @PatchMapping("/{id}/status") @PreAuthorize("hasAnyRole('ADMIN','STAFF')") @Operation(summary = "Update table status")
    public ResponseEntity<ApiResponse<TableResponse>> updateStatus(@PathVariable Long id, @RequestParam TableStatus status) { return ResponseEntity.ok(ApiResponse.success("Status updated", tableService.updateTableStatus(id, status))); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @Operation(summary = "Delete table")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) { tableService.deleteTable(id); return ResponseEntity.ok(ApiResponse.success("Deleted", null)); }
}