package com.example.restaurantmanagement.mapper;
import com.example.restaurantmanagement.dto.response.PaymentResponse;
import com.example.restaurantmanagement.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(source = "order.id", target = "orderId")
    PaymentResponse toResponse(Payment payment);
    List<PaymentResponse> toResponseList(List<Payment> payments);
}