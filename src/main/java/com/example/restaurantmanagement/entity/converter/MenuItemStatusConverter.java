 package com.example.restaurantmanagement.entity.converter;

import com.example.restaurantmanagement.entity.enums.MenuItemStatus;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MenuItemStatusConverter implements AttributeConverter<MenuItemStatus, String> {

    @Override
    public String convertToDatabaseColumn(MenuItemStatus attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public MenuItemStatus convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return null;
        }
        return MenuItemStatus.valueOf(dbData);
    }
}

