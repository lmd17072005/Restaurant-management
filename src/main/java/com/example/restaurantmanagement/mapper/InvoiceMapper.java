package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.response.InvoiceResponse;
import com.example.restaurantmanagement.dto.response.OrderResponse;
import com.example.restaurantmanagement.dto.response.PaymentResponse;
import com.example.restaurantmanagement.entity.Invoice;
import com.example.restaurantmanagement.entity.Order;
import com.example.restaurantmanagement.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InvoiceMapper {
    @Mapping(source = "table.id",           target = "tableId")
    @Mapping(source = "table.tableCode",    target = "tableCode")
    @Mapping(source = "customer.id",        target = "customerId")
    @Mapping(source = "customer.fullName",  target = "customerName")
    @Mapping(source = "openedBy.id",        target = "openedById")
    @Mapping(source = "openedBy.fullName",  target = "openedByName")
    InvoiceResponse toResponse(Invoice invoice);

    List<InvoiceResponse> toResponseList(List<Invoice> invoices);

    @Mapping(source = "invoice.id",          target = "invoiceId")
    @Mapping(source = "createdBy.id",        target = "createdById")
    @Mapping(source = "createdBy.fullName",  target = "createdByName")
    @Mapping(source = "menuItem.id",         target = "menuItemId")    // ← thêm
    @Mapping(source = "menuItem.name",       target = "menuItemName")  // ← thêm
    OrderResponse toOrderResponse(Order order);


    @Mapping(source = "invoice.id",           target = "invoiceId")
    @Mapping(source = "processedBy.id",       target = "processedById")
    @Mapping(source = "processedBy.fullName", target = "processedByName")
    PaymentResponse toPaymentResponse(Payment payment);
}

