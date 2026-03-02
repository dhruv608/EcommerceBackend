# 🛒 Cart API Documentation

## Base URL
```
http://localhost:8080
```

## 📋 Table of Contents
- [Cart Overview](#-cart-overview)
- [Cart APIs](#-cart-apis)
- [Error Responses](#-error-responses)
- [Data Models](#-data-models)
- [Usage Examples](#-usage-examples)

---

## 🛒 Cart Overview

The cart system allows users to:
- Add products to cart
- View cart items with product details
- Update item quantities
- Remove items from cart
- Clear entire cart
- Get cart summary with totals
- Track item count

Each user has their own cart based on `userId`.

---

## 🛒 Cart APIs

### 1. Add Item to Cart
**Endpoint:** `POST /cart/add`

**Description:** Add a product to user's cart or update quantity if already exists

**Query Parameters:**
- `userId` (required): User ID

**Request Body:**
```json
{
    "productId": 1,
    "quantity": 2
}
```

**Response (201 Created):**
```json
{
    "cartId": 1,
    "productId": 1,
    "productName": "Men's Casual Linen Shirt",
    "productDescription": "Regular fit long sleeve textured linen shirt, perfect for summer and casual wear",
    "productPrice": 499.00,
    "productImage": "https://example.com/image1.jpg",
    "quantity": 2,
    "totalPrice": 998.00,
    "stock": 50,
    "createdAt": "2026-03-03T01:00:00.000",
    "categoryId": 1,
    "categoryName": "Mens"
}
```

**Example:**
```bash
curl -X POST "http://localhost:8080/cart/add?userId=1" \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}'
```

---

### 2. Get Cart Items
**Endpoint:** `GET /cart`

**Description:** Get all items in user's cart

**Query Parameters:**
- `userId` (required): User ID

**Response (200 OK):**
```json
[
    {
        "cartId": 1,
        "productId": 1,
        "productName": "Men's Casual Linen Shirt",
        "productDescription": "Regular fit long sleeve textured linen shirt, perfect for summer and casual wear",
        "productPrice": 499.00,
        "productImage": "https://example.com/image1.jpg",
        "quantity": 2,
        "totalPrice": 998.00,
        "stock": 50,
        "createdAt": "2026-03-03T01:00:00.000",
        "categoryId": 1,
        "categoryName": "Mens"
    },
    {
        "cartId": 2,
        "productId": 2,
        "productName": "Women's Summer Dress",
        "productDescription": "Light and comfortable summer dress",
        "productPrice": 299.00,
        "productImage": "https://example.com/image2.jpg",
        "quantity": 1,
        "totalPrice": 299.00,
        "stock": 30,
        "createdAt": "2026-03-03T01:05:00.000",
        "categoryId": 2,
        "categoryName": "Womens"
    }
]
```

**Example:**
```bash
curl -X GET "http://localhost:8080/cart?userId=1"
```

---

### 3. Update Cart Item Quantity
**Endpoint:** `PUT /cart/update`

**Description:** Update quantity of an item in cart

**Query Parameters:**
- `userId` (required): User ID
- `productId` (required): Product ID
- `quantity` (required): New quantity

**Response (200 OK):**
```json
{
    "cartId": 1,
    "productId": 1,
    "productName": "Men's Casual Linen Shirt",
    "productDescription": "Regular fit long sleeve textured linen shirt, perfect for summer and casual wear",
    "productPrice": 499.00,
    "productImage": "https://example.com/image1.jpg",
    "quantity": 3,
    "totalPrice": 1497.00,
    "stock": 50,
    "createdAt": "2026-03-03T01:00:00.000",
    "categoryId": 1,
    "categoryName": "Mens"
}
```

**Example:**
```bash
curl -X PUT "http://localhost:8080/cart/update?userId=1&productId=1&quantity=3"
```

---

### 4. Remove Item from Cart
**Endpoint:** `DELETE /cart/remove`

**Description:** Remove a specific item from cart

**Query Parameters:**
- `userId` (required): User ID
- `productId` (required): Product ID

**Response (200 OK):**
```json
{
    "message": "Item removed from cart successfully"
}
```

**Example:**
```bash
curl -X DELETE "http://localhost:8080/cart/remove?userId=1&productId=1"
```

---

### 5. Clear Cart
**Endpoint:** `DELETE /cart/clear`

**Description:** Remove all items from user's cart

**Query Parameters:**
- `userId` (required): User ID

**Response (200 OK):**
```json
{
    "message": "Cart cleared successfully"
}
```

**Example:**
```bash
curl -X DELETE "http://localhost:8080/cart/clear?userId=1"
```

---

### 6. Get Cart Item Count
**Endpoint:** `GET /cart/count`

**Description:** Get total number of items in cart

**Query Parameters:**
- `userId` (required): User ID

**Response (200 OK):**
```json
{
    "itemCount": 3
}
```

**Example:**
```bash
curl -X GET "http://localhost:8080/cart/count?userId=1"
```

---

### 7. Get Cart Summary
**Endpoint:** `GET /cart/summary`

**Description:** Get cart summary with items, count, and total amount

**Query Parameters:**
- `userId` (required): User ID

**Response (200 OK):**
```json
{
    "itemCount": 2,
    "totalAmount": 1297.00,
    "items": [
        {
            "cartId": 1,
            "productId": 1,
            "productName": "Men's Casual Linen Shirt",
            "productDescription": "Regular fit long sleeve textured linen shirt, perfect for summer and casual wear",
            "productPrice": 499.00,
            "productImage": "https://example.com/image1.jpg",
            "quantity": 2,
            "totalPrice": 998.00,
            "stock": 50,
            "createdAt": "2026-03-03T01:00:00.000",
            "categoryId": 1,
            "categoryName": "Mens"
        },
        {
            "cartId": 2,
            "productId": 2,
            "productName": "Women's Summer Dress",
            "productDescription": "Light and comfortable summer dress",
            "productPrice": 299.00,
            "productImage": "https://example.com/image2.jpg",
            "quantity": 1,
            "totalPrice": 299.00,
            "stock": 30,
            "createdAt": "2026-03-03T01:05:00.000",
            "categoryId": 2,
            "categoryName": "Womens"
        }
    ]
}
```

**Example:**
```bash
curl -X GET "http://localhost:8080/cart/summary?userId=1"
```

---

## ❌ Error Responses

### 400 Bad Request
```json
{
    "timestamp": "2026-03-03T01:00:00.000",
    "status": 400,
    "error": "Bad Request",
    "message": "Quantity must be at least 1"
}
```

### 404 Not Found
```json
{
    "timestamp": "2026-03-03T01:00:00.000",
    "status": 404,
    "error": "Not Found",
    "message": "User not found with id: 999"
}
```

### 409 Conflict
```json
{
    "timestamp": "2026-03-03T01:00:00.000",
    "status": 409,
    "error": "Conflict",
    "message": "Insufficient stock. Available: 5, Requested: 10"
}
```

### 500 Internal Server Error
```json
{
    "timestamp": "2026-03-03T01:00:00.000",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Product is not available: Product Name"
}
```

---

## 📊 Data Models

### CartRequest
```json
{
    "productId": 1,
    "quantity": 2
}
```

### CartItemResponse
```json
{
    "cartId": 1,
    "productId": 1,
    "productName": "Men's Casual Linen Shirt",
    "productDescription": "Regular fit long sleeve textured linen shirt, perfect for summer and casual wear",
    "productPrice": 499.00,
    "productImage": "https://example.com/image1.jpg",
    "quantity": 2,
    "totalPrice": 998.00,
    "stock": 50,
    "createdAt": "2026-03-03T01:00:00.000",
    "categoryId": 1,
    "categoryName": "Mens"
}
```

### Cart Summary
```json
{
    "itemCount": 2,
    "totalAmount": 1297.00,
    "items": [CartItemResponse]
}
```

---

## 🚀 Usage Examples

### Complete Cart Flow
```bash
# 1. Add first item to cart
curl -X POST "http://localhost:8080/cart/add?userId=1" \
  -H "Content-Type: application/json" \
  -d '{"productId": 1, "quantity": 2}'

# 2. Add second item to cart
curl -X POST "http://localhost:8080/cart/add?userId=1" \
  -H "Content-Type: application/json" \
  -d '{"productId": 2, "quantity": 1}'

# 3. Get cart items
curl -X GET "http://localhost:8080/cart?userId=1"

# 4. Get cart summary
curl -X GET "http://localhost:8080/cart/summary?userId=1"

# 5. Update item quantity
curl -X PUT "http://localhost:8080/cart/update?userId=1&productId=1&quantity=3"

# 6. Get item count
curl -X GET "http://localhost:8080/cart/count?userId=1"

# 7. Remove specific item
curl -X DELETE "http://localhost:8080/cart/remove?userId=1&productId=2"

# 8. Clear entire cart
curl -X DELETE "http://localhost:8080/cart/clear?userId=1"
```

---

## 📱 Frontend Integration

```javascript
// Cart API Base URL
const CART_API = 'http://localhost:8080/cart';

// Cart Service
const cartService = {
  // Add item to cart
  addToCart: (userId, productId, quantity) => 
    fetch(`${CART_API}/add?userId=${userId}`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ productId, quantity })
    }).then(res => res.json()),

  // Get cart items
  getCartItems: (userId) => 
    fetch(`${CART_API}?userId=${userId}`).then(res => res.json()),

  // Update cart item
  updateCartItem: (userId, productId, quantity) => 
    fetch(`${CART_API}/update?userId=${userId}&productId=${productId}&quantity=${quantity}`, {
      method: 'PUT'
    }).then(res => res.json()),

  // Remove item from cart
  removeFromCart: (userId, productId) => 
    fetch(`${CART_API}/remove?userId=${userId}&productId=${productId}`, {
      method: 'DELETE'
    }).then(res => res.json()),

  // Clear cart
  clearCart: (userId) => 
    fetch(`${CART_API}/clear?userId=${userId}`, {
      method: 'DELETE'
    }).then(res => res.json()),

  // Get cart count
  getCartCount: (userId) => 
    fetch(`${CART_API}/count?userId=${userId}`).then(res => res.json()),

  // Get cart summary
  getCartSummary: (userId) => 
    fetch(`${CART_API}/summary?userId=${userId}`).then(res => res.json())
};

// Example Usage
const exampleUsage = async () => {
  const userId = 1;
  
  try {
    // Add to cart
    await cartService.addToCart(userId, 1, 2);
    
    // Get cart summary
    const summary = await cartService.getCartSummary(userId);
    console.log('Cart Summary:', summary);
    
    // Update quantity
    await cartService.updateCartItem(userId, 1, 3);
    
    // Get updated count
    const count = await cartService.getCartCount(userId);
    console.log('Item Count:', count.itemCount);
    
  } catch (error) {
    console.error('Cart Error:', error);
  }
};
```

---

## 🔧 Business Logic

### Stock Validation
- Checks product availability before adding to cart
- Validates stock on quantity updates
- Prevents adding more items than available stock

### Duplicate Handling
- If product already exists in cart, updates quantity instead of creating duplicate
- Sums existing quantity with new quantity

### Product Status
- Only allows adding active products to cart
- Prevents adding inactive/out-of-stock products

### Price Calculation
- Automatically calculates total price per item
- Summary endpoint calculates cart total amount

---

## 🎯 Features Summary

✅ **Add to Cart** - Add products with quantity validation  
✅ **View Cart** - Get all items with product details  
✅ **Update Quantity** - Modify item quantities with stock check  
✅ **Remove Items** - Remove specific items from cart  
✅ **Clear Cart** - Empty entire cart  
✅ **Item Count** - Get total number of items  
✅ **Cart Summary** - Complete cart with totals  
✅ **Stock Validation** - Prevents overselling  
✅ **User Isolation** - Each user has separate cart  
✅ **Product Details** - Rich product information in response  

Perfect for e-commerce applications! 🛒✨
