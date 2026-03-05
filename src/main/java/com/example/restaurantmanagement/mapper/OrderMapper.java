package com.example.restaurantmanagement.mapper;
import com.example.restaurantmanagement.dto.response.OrderItemResponse;
import com.example.restaurantmanagement.dto.response.OrderResponse;
import com.example.restaurantmanagement.entity.Order;
import com.example.restaurantmanagement.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
@Mapper(componentModel = "spring")
public interface OrderMapper {
    @Mapping(source = "table.id", target = "tableId")
    @Mapping(source = "table.tableNumber", target = "tableNumber")
    @Mapping(source = "user.fullName", target = "staffName")
    @Mapping(source = "orderItems", target = "items")
    OrderResponse toResponse(Order order);
    List<OrderResponse> toResponseList(List<Order> orders);
    @Mapping(source = "menuItem.id", target = "menuItemId")
    @Mapping(source = "menuItem.name", target = "menuItemName")
    @Mapping(target = "subtotal", expression = "java(orderItem.getUnitPrice().multiply(java.math.BigDecimal.valueOf(orderItem.getQuantity())))")
    OrderItemResponse toItemResponse(OrderItem orderItem);
}