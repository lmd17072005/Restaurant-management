package com.example.restaurantmanagement.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationMessage {
    
    // ======== COMMON ========
    SUCCESS("Thành công"),
    CREATED_SUCCESS("Tạo mới thành công"),
    UPDATED_SUCCESS("Cập nhật thành công"),
    DELETED_SUCCESS("Xóa thành công"),
    STATUS_UPDATED_SUCCESS("Cập nhật trạng thái thành công"),
    
    // ======== AUTH ========
    AUTH_REGISTER_SUCCESS("Đăng ký tài khoản thành công"),
    AUTH_LOGIN_SUCCESS("Đăng nhập thành công"),
    AUTH_INVALID_CREDENTIALS("Tên đăng nhập hoặc mật khẩu không đúng"),
    AUTH_ACCESS_DENIED("Bạn không có quyền thực hiện thao tác này"),
    AUTH_UNAUTHORIZED("Vui lòng đăng nhập để tiếp tục"),

    // ======== MENU ITEM ========
    MENU_ITEM_CREATED_SUCCESS("Tạo món ăn thành công"),
    MENU_ITEM_UPDATED_SUCCESS("Cập nhật thông tin món ăn thành công"),
    MENU_ITEM_DELETED_SUCCESS("Xóa món ăn thành công"),
    MENU_ITEM_STATUS_UPDATED_SUCCESS("Cập nhật trạng thái món ăn thành công"),
    MENU_ITEM_NOT_FOUND("Không tìm thấy món ăn"),
    MENU_ITEM_DUPLICATE_NAME("Tên món ăn đã tồn tại"),

    // ======== CATEGORY ========
    CATEGORY_CREATED_SUCCESS("Tạo danh mục thành công"),
    CATEGORY_UPDATED_SUCCESS("Cập nhật danh mục thành công"),
    CATEGORY_DELETED_SUCCESS("Xóa danh mục thành công"),
    CATEGORY_NOT_FOUND("Không tìm thấy danh mục"),
    CATEGORY_DUPLICATE_NAME("Tên danh mục đã tồn tại"),

    // ======== INGREDIENT ========
    INGREDIENT_CREATED_SUCCESS("Tạo nguyên liệu thành công"),
    INGREDIENT_UPDATED_SUCCESS("Cập nhật nguyên liệu thành công"),
    INGREDIENT_DELETED_SUCCESS("Xóa nguyên liệu thành công"),
    INGREDIENT_NOT_FOUND("Không tìm thấy nguyên liệu"),

    // ======== INVENTORY HISTORY ========
    INVENTORY_HISTORY_CREATED_SUCCESS("Tạo bản ghi kiểm kê thành công"),
    INVENTORY_HISTORY_NOT_FOUND("Không tìm thấy lịch sử kiểm kê"),

    // ======== INVOICE ========
    INVOICE_CREATED_SUCCESS("Mở hóa đơn thành công"),
    INVOICE_DISCOUNT_UPDATED("Cập nhật giảm giá hóa đơn thành công"),
    INVOICE_CANCELLED_SUCCESS("Hủy hóa đơn thành công"),
    INVOICE_NOT_FOUND("Không tìm thấy hóa đơn"),

    // ======== ORDER ========
    ORDER_CREATED_SUCCESS("Tạo đơn hàng (gọi món) thành công"),
    ORDER_STATUS_UPDATED_SUCCESS("Cập nhật trạng thái đơn hàng thành công"),
    ORDER_DELETED_SUCCESS("Xóa đơn hàng thành công"),
    ORDER_NOT_FOUND("Không tìm thấy đơn hàng"),

    // ======== PAYMENT ========
    PAYMENT_CREATED_SUCCESS("Thanh toán hóa đơn thành công"),
    PAYMENT_NOT_FOUND("Không tìm thấy giao dịch thanh toán"),

    // ======== RECIPE ========
    RECIPE_CREATED_SUCCESS("Tạo công thức thành công"),
    RECIPE_UPDATED_SUCCESS("Cập nhật công thức thành công"),
    RECIPE_DELETED_SUCCESS("Xóa công thức thành công"),
    RECIPE_NOT_FOUND("Không tìm thấy công thức"),

    // ======== RESERVATION ========
    RESERVATION_CREATED_SUCCESS("Tạo đặt bàn thành công"),
    RESERVATION_UPDATED_SUCCESS("Cập nhật đặt bàn thành công"),
    RESERVATION_STATUS_UPDATED_SUCCESS("Cập nhật trạng thái đặt bàn thành công"),
    RESERVATION_DELETED_SUCCESS("Xóa đặt bàn thành công"),
    RESERVATION_NOT_FOUND("Không tìm thấy thông tin đặt bàn"),

    // ======== TABLE ========
    TABLE_CREATED_SUCCESS("Tạo bàn thành công"),
    TABLE_UPDATED_SUCCESS("Cập nhật bàn thành công"),
    TABLE_STATUS_UPDATED_SUCCESS("Cập nhật trạng thái bàn thành công"),
    TABLE_DELETED_SUCCESS("Xóa bàn thành công"),
    TABLE_NOT_FOUND("Không tìm thấy bàn"),
    TABLE_DUPLICATE_NUMBER("Số bàn đã tồn tại"),

    // ======== USER ========
    USER_ROLE_UPDATED_SUCCESS("Cập nhật phân quyền người dùng thành công"),
    USER_DELETED_SUCCESS("Xóa người dùng thành công"),
    USER_NOT_FOUND("Không tìm thấy người dùng"),
    
    // ======== SYSTEM & VALIDATION ========
    VALIDATION_FAILED("Dữ liệu không hợp lệ"),
    INTERNAL_SERVER_ERROR("Lỗi hệ thống, vui lòng thử lại sau"),
    DUPLICATE_RESOURCE("Dữ liệu đã tồn tại trong hệ thống"),
    RESOURCE_NOT_FOUND("Không tìm thấy dữ liệu yêu cầu"),
    BAD_REQUEST("Yêu cầu không hợp lệ");

    private final String message;

    // Optional helper method to get the enum name as errorCode
    public String getErrorCode() {
        return this.name();
    }
}
