package com.example.restaurantmanagement.mapper;
import com.example.restaurantmanagement.dto.response.ReservationResponse;
import com.example.restaurantmanagement.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;
@Mapper(componentModel = "spring")
public interface ReservationMapper {
    @Mapping(source = "table.id", target = "tableId")
    @Mapping(source = "table.tableNumber", target = "tableNumber")
    ReservationResponse toResponse(Reservation reservation);
    List<ReservationResponse> toResponseList(List<Reservation> reservations);
}