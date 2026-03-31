package com.example.restaurantmanagement.service.impl;

import com.example.restaurantmanagement.dto.request.InvoiceRequest;
import com.example.restaurantmanagement.dto.response.InvoiceResponse;
import com.example.restaurantmanagement.entity.Invoice;
import com.example.restaurantmanagement.entity.RestaurantTable;
import com.example.restaurantmanagement.entity.User;
import com.example.restaurantmanagement.entity.enums.InvoiceStatus;
import com.example.restaurantmanagement.entity.enums.TableStatus;
import com.example.restaurantmanagement.exception.BadRequestException;
import com.example.restaurantmanagement.exception.ResourceNotFoundException;
import com.example.restaurantmanagement.mapper.InvoiceMapper;
import com.example.restaurantmanagement.repository.InvoiceRepository;
import com.example.restaurantmanagement.repository.RestaurantTableRepository;
import com.example.restaurantmanagement.repository.UserRepository;
import com.example.restaurantmanagement.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final RestaurantTableRepository tableRepository;
    private final UserRepository userRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getAllInvoices() {
        return invoiceMapper.toResponseList(invoiceRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getInvoiceById(Long id) {
        return invoiceMapper.toResponse(invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InvoiceResponse> getInvoicesByStatus(InvoiceStatus status) {
        return invoiceMapper.toResponseList(invoiceRepository.findByStatus(status));
    }

    @Override
    @Transactional
    public InvoiceResponse createInvoice(InvoiceRequest request) {
        RestaurantTable table = tableRepository.findById(request.getTableId())
                .orElseThrow(() -> new ResourceNotFoundException("Table", "id", request.getTableId()));

        User currentUser = getCurrentUser();
        User customer = null;
        if (request.getCustomerId() != null) {
            customer = userRepository.findById(request.getCustomerId())
                    .orElseThrow(() -> new ResourceNotFoundException("Customer", "id", request.getCustomerId()));
        }

        String invoiceCode = generateInvoiceCode();

        Invoice invoice = Invoice.builder()
                .table(table)
                .customer(customer)
                .invoiceCode(invoiceCode)
                .discount(request.getDiscount() != null ? request.getDiscount() : BigDecimal.ZERO)
                .discountReason(request.getDiscountReason())
                .openedBy(currentUser)
                .status(InvoiceStatus.chua_thanh_toan)
                .build();

        // Update table status to dang_phuc_vu
        table.setStatus(TableStatus.dang_phuc_vu);
        tableRepository.save(table);

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional
    public InvoiceResponse updateDiscount(Long id, InvoiceRequest request) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id));

        if (invoice.getStatus() == InvoiceStatus.da_thanh_toan || invoice.getStatus() == InvoiceStatus.da_huy) {
            throw new BadRequestException("Cannot update closed or cancelled invoice");
        }

        if (request.getDiscount() != null) {
            invoice.setDiscount(request.getDiscount());
            invoice.setDiscountReason(request.getDiscountReason());
            invoice.setTotalAmount(invoice.getSubtotal().subtract(request.getDiscount()));
        }

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional
    public InvoiceResponse cancelInvoice(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice", "id", id));

        if (invoice.getStatus() == InvoiceStatus.da_thanh_toan) {
            throw new BadRequestException("Cannot cancel a paid invoice");
        }

        invoice.setStatus(InvoiceStatus.da_huy);
        invoice.setClosedAt(LocalDateTime.now());

        // Free the table
        RestaurantTable table = invoice.getTable();
        table.setStatus(TableStatus.trong);
        tableRepository.save(table);

        return invoiceMapper.toResponse(invoiceRepository.save(invoice));
    }

    @Override
    @Transactional(readOnly = true)
    public InvoiceResponse getOpenInvoiceByTable(Integer tableId) {
        return invoiceRepository
                .findLatestOpenInvoiceByTableId(tableId)
                .map(invoiceMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Open invoice", "tableId", tableId));
    }

    private String generateInvoiceCode() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long count = invoiceRepository.findAll().stream()
                .filter(i -> i.getInvoiceCode().startsWith("HD-" + dateStr))
                .count();
        return "HD-" + dateStr + "-" + String.format("%03d", count + 1);
    }

    private User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof User) {
            return (User) auth.getPrincipal();
        }
        throw new RuntimeException("User not authenticated");
    }
}

