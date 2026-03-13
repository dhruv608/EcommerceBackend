# 🛒 Order Management API Documentation

## 📋 Table of Contents
- [Database Schema](#-database-schema)
- [Order APIs](#-order-apis)
- [Usage Examples](#-usage-examples)

---

## 🗄️ Database Schema

### **Orders Table**
```sql
CREATE TABLE orders (
    id BIGINT PRIMARY KEY,
    order_number VARCHAR(50) UNIQUE NOT NULL,
    user_id BIGINT REFERENCES users(id),
    total_amount DECIMAL(10,2) NOT NULL,
    total_items INT NOT NULL,
    status VARCHAR(50) DEFAULT 'PENDING',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### **Order Items Table**
```sql
CREATE TABLE order_items (
    id BIGINT PRIMARY KEY,
    order_id BIGINT REFERENCES orders(id),
    product_id BIGINT REFERENCES products(id),
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL
);
```

---

## 🛒 Order APIs

### **POST /orders**
**Description:** Create a new order with multiple products

**Request:**
```json
POST /orders
Content-Type: application/json

{
    "userId": 6,
    "items": [
        {
            "productId": 1,
            "quantity": 2
        },
        {
            "productId": 2,
            "quantity": 1
        }
    ]
}
```

**Response (201 Created):**
```json
{
    "id": 1,
    "orderNumber": "ORD123456",
    "userId": 6,
    "userName": "John Doe",
    "userEmail": "john@example.com",
    "totalAmount": 1497.00,
    "totalItems": 3,
    "status": "PENDING",
    "orderDate": "2026-03-03T01:00:00.000000",
    "orderItems": [
        {
            "id": 1,
            "productId": 1,
            "productName": "Men's Casual Linen Shirt",
            "productDescription": "Regular fit long sleeve textured linen shirt",
            "price": 499.00,
            "quantity": 2,
            "subtotal": 998.00
        },
        {
            "id": 2,
            "productId": 2,
            "productName": "Women's Summer Dress",
            "productDescription": "Lightweight summer dress",
            "price": 499.00,
            "quantity": 1,
            "subtotal": 499.00
        }
    ]
}
```

**Response (400 Bad Request):**
```json
{
    "message": "User not found",
    "status": "BAD_REQUEST"
}
```

---

### **GET /orders/user/{userId}**
**Description:** Get all orders for a specific user

**Request:**
```http
GET /orders/user/6
```

**Response (200 OK):**
```json
[
    {
        "id": 1,
        "orderNumber": "ORD123456",
        "userId": 6,
        "userName": "John Doe",
        "userEmail": "john@example.com",
        "totalAmount": 1497.00,
        "totalItems": 3,
        "status": "PENDING",
        "orderDate": "2026-03-03T01:00:00.000000",
        "orderItems": [
            {
                "id": 1,
                "productId": 1,
                "productName": "Men's Casual Linen Shirt",
                "productDescription": "Regular fit long sleeve textured linen shirt",
                "price": 499.00,
                "quantity": 2,
                "subtotal": 998.00
            }
        ]
    }
]
```

---

### **GET /orders/number/{orderNumber}**
**Description:** Get order by order number

**Request:**
```http
GET /orders/number/ORD123456
```

**Response (200 OK):**
```json
{
    "id": 1,
    "orderNumber": "ORD123456",
    "userId": 6,
    "userName": "John Doe",
    "userEmail": "john@example.com",
    "totalAmount": 1497.00,
    "totalItems": 3,
    "status": "PENDING",
    "orderDate": "2026-03-03T01:00:00.000000",
    "orderItems": [
        {
            "id": 1,
            "productId": 1,
            "productName": "Men's Casual Linen Shirt",
            "productDescription": "Regular fit long sleeve textured linen shirt",
            "price": 499.00,
            "quantity": 2,
            "subtotal": 998.00
        }
    ]
}
```

---

### **GET /orders**
**Description:** Get all orders (admin function)

**Request:**
```http
GET /orders
```

**Response (200 OK):**
```json
[
    {
        "id": 1,
        "orderNumber": "ORD123456",
        "userId": 6,
        "userName": "John Doe",
        "userEmail": "john@example.com",
        "totalAmount": 1497.00,
        "totalItems": 3,
        "status": "PENDING",
        "orderDate": "2026-03-03T01:00:00.000000",
        "orderItems": [...]
    }
]
```

---

### **PUT /orders/{orderNumber}/status**
**Description:** Update order status

**Request:**
```http
PUT /orders/ORD123456/status?status=CONFIRMED
```

**Response (200 OK):**
```json
{
    "id": 1,
    "orderNumber": "ORD123456",
    "userId": 6,
    "userName": "John Doe",
    "userEmail": "john@example.com",
    "totalAmount": 1497.00,
    "totalItems": 3,
    "status": "CONFIRMED",
    "orderDate": "2026-03-03T01:00:00.000000",
    "orderItems": [...]
}
```

---

## 📱 Usage Examples

### **PowerShell Commands:**
```powershell
# Create Order
Invoke-WebRequest -Uri "https://ecommercebackend-fqk1.onrender.com/orders" -Method POST -ContentType "application/json" -Body '{"userId": 6, "items": [{"productId": 1, "quantity": 2}]}'

# Get User Orders
Invoke-WebRequest -Uri "https://ecommercebackend-fqk1.onrender.com/orders/user/6" -Method GET

# Get Order by Number
Invoke-WebRequest -Uri "https://ecommercebackend-fqk1.onrender.com/orders/number/ORD123456" -Method GET

# Update Order Status
Invoke-WebRequest -Uri "https://ecommercebackend-fqk1.onrender.com/orders/ORD123456/status?status=CONFIRMED" -Method PUT
```

### **JavaScript/Fetch Examples:**
```javascript
// Create Order
fetch('https://ecommercebackend-fqk1.onrender.com/orders', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
        userId: 6,
        items: [
            { productId: 1, quantity: 2 },
            { productId: 2, quantity: 1 }
        ]
    })
});

// Get User Orders
fetch('https://ecommercebackend-fqk1.onrender.com/orders/user/6');

// Get Order by Number
fetch('https://ecommercebackend-fqk1.onrender.com/orders/number/ORD123456');

// Update Order Status
fetch('https://ecommercebackend-fqk1.onrender.com/orders/ORD123456/status?status=CONFIRMED', {
    method: 'PUT'
});
```

---

## 🎯 Order Features

### **✅ What's Included:**
- 🛒 **Order Creation** - Multiple products in single order
- 📊 **Order Tracking** - Unique order numbers
- 👤 **User Orders** - View all user's orders
- 📋 **Order Details** - Complete order information
- 🔄 **Status Updates** - Change order status
- 💰 **Price Calculation** - Automatic total calculation

### **🔧 Order Status Options:**
- `PENDING` - Order created, awaiting confirmation
- `CONFIRMED` - Order confirmed by admin
- `PROCESSING` - Order being prepared
- `SHIPPED` - Order shipped to customer
- `DELIVERED` - Order delivered successfully
- `CANCELLED` - Order cancelled

### **📊 Order Information:**
- **Order Number** - Unique identifier (ORD123456)
- **User Details** - Name and email
- **Product Details** - Name, description, price
- **Quantity** - Number of items
- **Subtotal** - Item total
- **Total Amount** - Order total
- **Order Date** - Creation timestamp
- **Status** - Current order status

---

## 🌐 Live API

**Base URL:** `https://ecommercebackend-fqk1.onrender.com`

**Order Endpoints:**
- `POST /orders` - Create order
- `GET /orders/user/{userId}` - Get user orders
- `GET /orders/number/{orderNumber}` - Get order by number
- `GET /orders` - Get all orders
- `PUT /orders/{orderNumber}/status` - Update status

---

## 🎉 Order Management Complete!

**Your e-commerce backend now includes full order management!** 🛒✨

**Features:**
- ✅ **Order creation** with multiple products
- ✅ **Random order numbers** (ORD123456 format)
- ✅ **User order history**
- ✅ **Order status tracking**
- ✅ **Complete order details**
- ✅ **Price calculations**

**Perfect for frontend integration!** 🚀
