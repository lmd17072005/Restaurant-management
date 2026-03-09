package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.response.PaymentResponse;
import com.example.restaurantmanagement.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "invoice.id", target = "invoiceId")
    @Mapping(source = "processedBy.id", target = "processedById")
    @Mapping(source = "processedBy.fullName", target = "processedByName")
    PaymentResponse toResponse(Payment payment);

    List<PaymentResponse> toResponseList(List<Payment> payments);
}

