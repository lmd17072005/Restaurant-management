package com.example.restaurantmanagement.mapper;

import com.example.restaurantmanagement.dto.response.UserResponse;
import com.example.restaurantmanagement.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toResponse(User user);
    List<UserResponse> toResponseList(List<User> users);
}

