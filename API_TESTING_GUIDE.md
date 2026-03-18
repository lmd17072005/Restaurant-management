# Restaurant Management API - Testing Guide

**Base URL:** `http://localhost:8081`
**API Documentation:** `http://localhost:8081/swagger-ui/index.html`
**OpenAPI JSON:** `http://localhost:8081/api-docs`

> [!TIP]
> **Getting "Socket Hang Up" in Postman?**
> 1. **Wait for startup**: The app takes 1-2 minutes to connect to Supabase. Check logs: `docker compose logs -f backend`.
> 2. **Check Port**: Ensure you are using `8081`, not `8080`.
> 3. **Docker Running?**: Ensure your container is "Running" in Docker Desktop.

---

## Table of Contents
- [Authentication](#authentication)
- [Users](#users)
- [Categories](#categories)
- [Menu Items](#menu-items)
- [Tables](#tables)
- [Reservations](#reservations)
- [Invoices](#invoices)
- [Orders](#orders)
- [Payments](#payments)
- [Ingredients](#ingredients)
- [Recipes](#recipes)
- [Inventory History](#inventory-history)
- [Audit Logs](#audit-logs)
- [Response Format](#response-format)
- [Authorization Roles](#authorization-roles)

---

## Authentication

### Register New User
**POST** `/api/v1/auth/register`
**Auth Required:** No

**Request Body:**
```json
{
  "username": "testuser",
  "password": "password123",
  "fullName": "Test User",
  "phone": "0912345678",
  "email": "test@example.com"
}
```

**Response (201):**
```json
{
  "success": true,
  "status": 201,
  "message": "Created successfully",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "...",
    "user": {
      "id": 1,
      "username": "testuser",
      "fullName": "Test User",
      "email": "test@example.com",
      "phone": "0912345678",
      "role": "KHACH_HANG"
    }
  },
  "timestamp": "2026-03-18T08:30:00"
}
```

---

### Login
**POST** `/api/v1/auth/login`
**Auth Required:** No

**Request Body:**
```json
{
  "username": "testuser",
  "password": "password123"
}
```

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "message": "Login successful",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "...",
    "user": {
      "id": 1,
      "username": "testuser",
      "fullName": "Test User",
      "role": "KHACH_HANG"
    }
  },
  "timestamp": "2026-03-18T08:30:00"
}
```

**Note:** Copy the `token` value and use it as Bearer token for protected endpoints.

---

## Users

### Get All Users
**GET** `/api/v1/users`
**Auth Required:** Yes (QUAN_LY only)

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "id": 1,
      "username": "admin",
      "fullName": "Administrator",
      "email": "admin@example.com",
      "role": "QUAN_LY"
    }
  ]
}
```

---

### Get User by ID
**GET** `/api/v1/users/{id}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `GET /api/v1/users/1`

---

### Get Users by Role
**GET** `/api/v1/users/role/{role}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `GET /api/v1/users/role/NHAN_VIEN`

**Available Roles:**
- `QUAN_LY` - Manager
- `NHAN_VIEN` - Staff
- `KHACH_HANG` - Customer

---

### Update User Role
**PATCH** `/api/v1/users/{id}/role?role={role}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `PATCH /api/v1/users/5/role?role=NHAN_VIEN`

---

### Delete User
**DELETE** `/api/v1/users/{id}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `DELETE /api/v1/users/5`

---

## Categories

### Get All Categories
**GET** `/api/v1/categories`
**Auth Required:** No

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "id": 1,
      "name": "Appetizers",
      "description": "Starters and small dishes"
    }
  ]
}
```

---

### Get Category by ID
**GET** `/api/v1/categories/{id}`
**Auth Required:** No

**Example:** `GET /api/v1/categories/1`

---

### Create Category
**POST** `/api/v1/categories`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "name": "Desserts",
  "description": "Sweet treats and desserts"
}
```

**Response (201):**
```json
{
  "success": true,
  "status": 201,
  "message": "Created successfully",
  "data": {
    "id": 5,
    "name": "Desserts",
    "description": "Sweet treats and desserts"
  }
}
```

---

### Update Category
**PUT** `/api/v1/categories/{id}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "name": "Desserts & Sweets",
  "description": "Updated description"
}
```

---

### Delete Category
**DELETE** `/api/v1/categories/{id}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `DELETE /api/v1/categories/5`

---

## Menu Items

### Get All Menu Items
**GET** `/api/v1/menu-items`
**Auth Required:** No

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "id": 1,
      "name": "Grilled Chicken",
      "description": "Juicy grilled chicken with herbs",
      "price": 150000,
      "categoryId": 2,
      "categoryName": "Main Course",
      "isAvailable": true
    }
  ]
}
```

---

### Get Menu Item by ID
**GET** `/api/v1/menu-items/{id}`
**Auth Required:** No

**Example:** `GET /api/v1/menu-items/1`

---

### Get Menu Items by Category
**GET** `/api/v1/menu-items/category/{categoryId}`
**Auth Required:** No

**Example:** `GET /api/v1/menu-items/category/2`

---

### Search Menu Items
**GET** `/api/v1/menu-items/search?keyword={keyword}`
**Auth Required:** No

**Example:** `GET /api/v1/menu-items/search?keyword=chicken`

---

### Create Menu Item
**POST** `/api/v1/menu-items`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "name": "Pad Thai",
  "description": "Traditional Thai stir-fried noodles",
  "price": 120000,
  "categoryId": 2,
  "isAvailable": true
}
```

---

### Update Menu Item
**PUT** `/api/v1/menu-items/{id}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "name": "Pad Thai Special",
  "description": "Traditional Thai stir-fried noodles with prawns",
  "price": 150000,
  "categoryId": 2,
  "isAvailable": true
}
```

---

### Delete Menu Item
**DELETE** `/api/v1/menu-items/{id}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `DELETE /api/v1/menu-items/10`

---

## Tables

### Get All Tables
**GET** `/api/v1/tables`
**Auth Required:** No

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "id": 1,
      "tableCode": "T01",
      "capacity": 4,
      "status": "AVAILABLE"
    }
  ]
}
```

---

### Get Table by ID
**GET** `/api/v1/tables/{id}`
**Auth Required:** No

**Example:** `GET /api/v1/tables/1`

---

### Get Tables by Status
**GET** `/api/v1/tables/status/{status}`
**Auth Required:** No

**Example:** `GET /api/v1/tables/status/AVAILABLE`

**Table Statuses:**
- `AVAILABLE` - Available for booking
- `OCCUPIED` - Currently occupied
- `RESERVED` - Reserved by customer
- `MAINTENANCE` - Under maintenance

---

### Create Table
**POST** `/api/v1/tables`
**Auth Required:** Yes (QUAN_LY only)

**Request Body:**
```json
{
  "tableCode": "T05",
  "capacity": 6,
  "status": "AVAILABLE"
}
```

---

### Update Table
**PUT** `/api/v1/tables/{id}`
**Auth Required:** Yes (QUAN_LY only)

**Request Body:**
```json
{
  "tableCode": "T05",
  "capacity": 8,
  "status": "AVAILABLE"
}
```

---

### Update Table Status
**PATCH** `/api/v1/tables/{id}/status?status={status}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `PATCH /api/v1/tables/1/status?status=OCCUPIED`

---

### Delete Table
**DELETE** `/api/v1/tables/{id}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `DELETE /api/v1/tables/5`

---

## Reservations

### Get All Reservations
**GET** `/api/v1/reservations`
**Auth Required:** No

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "id": 1,
      "tableId": 3,
      "customerId": 5,
      "customerName": "John Doe",
      "numberOfGuests": 4,
      "reservationTime": "2026-03-20T19:00:00",
      "status": "PENDING",
      "note": "Window seat preferred"
    }
  ]
}
```

---

### Get Reservation by ID
**GET** `/api/v1/reservations/{id}`
**Auth Required:** No

**Example:** `GET /api/v1/reservations/1`

---

### Get Reservations by Status
**GET** `/api/v1/reservations/status/{status}`
**Auth Required:** No

**Example:** `GET /api/v1/reservations/status/CONFIRMED`

**Reservation Statuses:**
- `PENDING` - Waiting for confirmation
- `CONFIRMED` - Confirmed by restaurant
- `CANCELLED` - Cancelled
- `COMPLETED` - Customer showed up

---

### Create Reservation
**POST** `/api/v1/reservations`
**Auth Required:** No

**Request Body:**
```json
{
  "tableId": 3,
  "customerId": 5,
  "numberOfGuests": 4,
  "reservationTime": "2026-03-20T19:00:00",
  "note": "Anniversary celebration"
}
```

---

### Update Reservation
**PUT** `/api/v1/reservations/{id}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "tableId": 3,
  "customerId": 5,
  "numberOfGuests": 6,
  "reservationTime": "2026-03-20T19:30:00",
  "note": "Updated guest count"
}
```

---

### Update Reservation Status
**PATCH** `/api/v1/reservations/{id}/status?status={status}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `PATCH /api/v1/reservations/1/status?status=CONFIRMED`

---

### Delete Reservation
**DELETE** `/api/v1/reservations/{id}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `DELETE /api/v1/reservations/10`

---

## Invoices

### Get All Invoices
**GET** `/api/v1/invoices`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "id": 1,
      "tableId": 3,
      "staffId": 2,
      "totalAmount": 450000,
      "discount": 50000,
      "finalAmount": 400000,
      "status": "UNPAID",
      "createdAt": "2026-03-18T12:00:00"
    }
  ]
}
```

---

### Get Invoice by ID
**GET** `/api/v1/invoices/{id}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `GET /api/v1/invoices/1`

---

### Get Invoices by Status
**GET** `/api/v1/invoices/status/{status}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `GET /api/v1/invoices/status/PAID`

**Invoice Statuses:**
- `UNPAID` - Not yet paid
- `PAID` - Payment completed
- `CANCELLED` - Invoice cancelled

---

### Create Invoice
**POST** `/api/v1/invoices`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "tableId": 3,
  "discount": 0
}
```

**Note:** This creates a new bill for a table. Orders will be added separately.

---

### Update Invoice Discount
**PUT** `/api/v1/invoices/{id}/discount`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "discount": 50000
}
```

---

### Cancel Invoice
**PATCH** `/api/v1/invoices/{id}/cancel`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `PATCH /api/v1/invoices/5/cancel`

---

## Orders

### Get All Orders
**GET** `/api/v1/orders`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "id": 1,
      "invoiceId": 1,
      "items": [
        {
          "menuItemId": 5,
          "menuItemName": "Pad Thai",
          "quantity": 2,
          "price": 120000,
          "subtotal": 240000
        }
      ],
      "totalAmount": 240000,
      "status": "PENDING",
      "createdAt": "2026-03-18T12:15:00"
    }
  ]
}
```

---

### Get Order by ID
**GET** `/api/v1/orders/{id}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `GET /api/v1/orders/1`

---

### Get Orders by Invoice
**GET** `/api/v1/orders/invoice/{invoiceId}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `GET /api/v1/orders/invoice/1`

---

### Get Orders by Status
**GET** `/api/v1/orders/status/{status}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `GET /api/v1/orders/status/COOKING`

**Order Statuses:**
- `PENDING` - Just ordered
- `COOKING` - Being prepared
- `READY` - Ready to serve
- `SERVED` - Served to customer
- `CANCELLED` - Order cancelled

---

### Create Order
**POST** `/api/v1/orders`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "invoiceId": 1,
  "items": [
    {
      "menuItemId": 5,
      "quantity": 2
    },
    {
      "menuItemId": 8,
      "quantity": 1
    }
  ]
}
```

---

### Update Order Status
**PATCH** `/api/v1/orders/{id}/status?status={status}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `PATCH /api/v1/orders/1/status?status=COOKING`

---

### Delete Order
**DELETE** `/api/v1/orders/{id}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `DELETE /api/v1/orders/5`

---

## Payments

### Get All Payments
**GET** `/api/v1/payments`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "id": 1,
      "invoiceId": 1,
      "amount": 400000,
      "paymentMethod": "CASH",
      "paymentTime": "2026-03-18T13:00:00"
    }
  ]
}
```

---

### Get Payment by ID
**GET** `/api/v1/payments/{id}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `GET /api/v1/payments/1`

---

### Get Payments by Invoice
**GET** `/api/v1/payments/invoice/{invoiceId}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `GET /api/v1/payments/invoice/1`

---

### Create Payment
**POST** `/api/v1/payments`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "invoiceId": 1,
  "amount": 400000,
  "paymentMethod": "CASH"
}
```

**Payment Methods:**
- `CASH` - Cash payment
- `CREDIT_CARD` - Credit card
- `DEBIT_CARD` - Debit card
- `BANK_TRANSFER` - Bank transfer
- `E_WALLET` - Digital wallet

**Note:** Creating a payment automatically updates the invoice status to PAID.

---

## Ingredients

### Get All Ingredients
**GET** `/api/v1/ingredients`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "id": 1,
      "name": "Chicken Breast",
      "unit": "kg",
      "currentStock": 50.5,
      "minStock": 10.0
    }
  ]
}
```

---

### Get Ingredient by ID
**GET** `/api/v1/ingredients/{id}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `GET /api/v1/ingredients/1`

---

### Create Ingredient
**POST** `/api/v1/ingredients`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "name": "Tomatoes",
  "unit": "kg",
  "currentStock": 20.0,
  "minStock": 5.0
}
```

---

### Update Ingredient
**PUT** `/api/v1/ingredients/{id}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "name": "Cherry Tomatoes",
  "unit": "kg",
  "currentStock": 25.0,
  "minStock": 5.0
}
```

---

### Delete Ingredient
**DELETE** `/api/v1/ingredients/{id}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `DELETE /api/v1/ingredients/10`

---

## Recipes

### Get All Recipes
**GET** `/api/v1/recipes`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "id": 1,
      "menuItemId": 5,
      "menuItemName": "Pad Thai",
      "ingredientId": 1,
      "ingredientName": "Rice Noodles",
      "quantity": 0.2
    }
  ]
}
```

---

### Get Recipe by ID
**GET** `/api/v1/recipes/{id}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `GET /api/v1/recipes/1`

---

### Get Recipes by Menu Item
**GET** `/api/v1/recipes/menu-item/{menuItemId}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `GET /api/v1/recipes/menu-item/5`

---

### Create Recipe
**POST** `/api/v1/recipes`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "menuItemId": 5,
  "ingredientId": 1,
  "quantity": 0.2
}
```

**Note:** Quantity is in the ingredient's unit (kg, liters, pieces, etc.)

---

### Update Recipe
**PUT** `/api/v1/recipes/{id}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "menuItemId": 5,
  "ingredientId": 1,
  "quantity": 0.25
}
```

---

### Delete Recipe
**DELETE** `/api/v1/recipes/{id}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `DELETE /api/v1/recipes/10`

---

## Inventory History

### Get All Inventory Histories
**GET** `/api/v1/inventory-histories`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "id": 1,
      "ingredientId": 1,
      "ingredientName": "Chicken Breast",
      "type": "IMPORT",
      "quantity": 20.0,
      "note": "Weekly stock replenishment",
      "createdBy": "admin",
      "createdAt": "2026-03-18T10:00:00"
    }
  ]
}
```

---

### Get Inventory History by ID
**GET** `/api/v1/inventory-histories/{id}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `GET /api/v1/inventory-histories/1`

---

### Get Histories by Ingredient
**GET** `/api/v1/inventory-histories/ingredient/{ingredientId}`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Example:** `GET /api/v1/inventory-histories/ingredient/1`

---

### Create Inventory History
**POST** `/api/v1/inventory-histories`
**Auth Required:** Yes (QUAN_LY, NHAN_VIEN)

**Request Body:**
```json
{
  "ingredientId": 1,
  "type": "IMPORT",
  "quantity": 15.0,
  "note": "New stock arrival"
}
```

**Inventory Types:**
- `IMPORT` - Stock coming in (increases stock)
- `EXPORT` - Stock going out (decreases stock)
- `ADJUSTMENT` - Manual adjustment (can be + or -)

**Note:** This automatically updates the ingredient's current stock.

---

## Audit Logs

### Get All Audit Logs
**GET** `/api/v1/audit-logs`
**Auth Required:** Yes (QUAN_LY only)

**Response (200):**
```json
{
  "success": true,
  "status": 200,
  "data": [
    {
      "id": 1,
      "tableName": "nguoi_dung",
      "recordId": 5,
      "action": "UPDATE",
      "oldValue": "{\"role\":\"KHACH_HANG\"}",
      "newValue": "{\"role\":\"NHAN_VIEN\"}",
      "userId": 1,
      "username": "admin",
      "timestamp": "2026-03-18T14:30:00"
    }
  ]
}
```

---

### Get Audit Log by ID
**GET** `/api/v1/audit-logs/{id}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `GET /api/v1/audit-logs/1`

---

### Search Audit Logs
**GET** `/api/v1/audit-logs/search?tableName={table}&recordId={id}`
**Auth Required:** Yes (QUAN_LY only)

**Example:** `GET /api/v1/audit-logs/search?tableName=nguoi_dung&recordId=5`

**Note:** This retrieves all audit logs for a specific record in a table.

---

## Response Format

All API responses follow this structure:

### Success Response
```json
{
  "success": true,
  "status": 200,
  "message": "Operation successful",
  "data": { ... },
  "timestamp": "2026-03-18T10:30:00"
}
```

### Error Response
```json
{
  "success": false,
  "status": 400,
  "message": "Validation error",
  "errors": {
    "username": "Username is required",
    "password": "Password must be at least 6 characters"
  },
  "timestamp": "2026-03-18T10:30:00"
}
```

### Common HTTP Status Codes
- `200` OK - Request succeeded
- `201` Created - Resource created successfully
- `400` Bad Request - Invalid request data
- `401` Unauthorized - Missing or invalid authentication
- `403` Forbidden - Insufficient permissions
- `404` Not Found - Resource not found
- `500` Internal Server Error - Server error

---

## Authorization Roles

### Role Hierarchy
1. **QUAN_LY** (Manager) - Full access to all endpoints
2. **NHAN_VIEN** (Staff) - Can manage orders, payments, menu items
3. **KHACH_HANG** (Customer) - Limited access, can create reservations

### Setting Authorization in Postman

**Method 1: Collection-Level (Recommended)**
1. Select your collection in the sidebar
2. Go to **Authorization** tab
3. Type: **Bearer Token**
4. Token: Paste your JWT token from login/register
5. Click **Save**

**Method 2: Request-Level**
1. Open any request
2. Go to **Authorization** tab
3. Type: **Bearer Token**
4. Token: Paste your JWT token

### Token Expiry
- **Access Token:** 24 hours (86400000 ms)
- **Refresh Token:** 7 days (604800000 ms)

When your token expires, simply login again to get a new token.

---

## Testing Workflow

### 1. Start Testing (Public Endpoints)
```
GET /api/v1/categories
GET /api/v1/menu-items
GET /api/v1/tables
```

### 2. Authentication
```
POST /api/v1/auth/register
POST /api/v1/auth/login (copy the token)
```

### 3. Set Bearer Token
Add the token to your collection's Authorization

### 4. Test Basic Operations
```
POST /api/v1/categories
POST /api/v1/menu-items
POST /api/v1/tables
```

### 5. Test Full Restaurant Flow
```
1. POST /api/v1/invoices (create bill for table)
2. POST /api/v1/orders (add items to invoice)
3. PATCH /api/v1/orders/{id}/status (update order status)
4. POST /api/v1/payments (complete payment)
```

### 6. Test Inventory Management
```
1. POST /api/v1/ingredients (add ingredient)
2. POST /api/v1/recipes (link ingredient to menu item)
3. POST /api/v1/inventory-histories (import/export stock)
```

---

## Tips

1. **Use Postman Environment Variables**
   - Create variable: `base_url` = `http://localhost:8081`
   - Create variable: `token` = `your_jwt_token`
   - Use: `{{base_url}}/api/v1/categories`

2. **Check Swagger UI**
   - Visit: `http://localhost:8081/swagger-ui/index.html`
   - Interactive documentation with try-it-out feature

3. **Monitor Logs**
   - Watch container logs: `docker logs -f restaurant-management-backend-1`
   - Check for SQL queries and error messages

4. **Test Error Cases**
   - Try invalid data to see validation errors
   - Test without authentication to see 401 errors
   - Test with wrong role to see 403 errors

---

**Happy Testing!** 🚀
