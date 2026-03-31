package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.response.OrderResponse;
import com.example.restaurantmanagement.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "invoice.id",         target = "invoiceId")
    @Mapping(source = "createdBy.id",       target = "createdById")
    @Mapping(source = "createdBy.fullName", target = "createdByName")
    @Mapping(source = "menuItem.id",        target = "menuItemId")
    @Mapping(source = "menuItem.name",      target = "menuItemName")
    OrderResponse toResponse(Order order);

    List<OrderResponse> toResponseList(List<Order> orders);
}

