# Complete API Documentation - Spring Boot E-commerce Backend

## Base URL
```
http://localhost:8080
```

## 📋 Table of Contents
- [Authentication APIs](#-authentication-apis)
- [Product APIs](#-product-apis)
- [Category APIs](#-category-apis)
- [Test APIs](#-test-apis)
- [Image Upload APIs](#-image-upload-apis)
- [Error Responses](#-error-responses)
- [Data Models](#-data-models)

---

## 🔐 Authentication APIs

### 1. User Registration
**Endpoint:** `POST /auth/register`

**Description:** Register a new user account

**Request Body:**
```json
{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
}
```

**Response (200 OK):**
```json
"User Registered Successfully"
```

**Example:**
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123"
  }'
```

---

### 2. User Login
**Endpoint:** `POST /auth/login`

**Description:** Authenticate user and return user info

**Request Body:**
```json
{
    "email": "john@example.com",
    "password": "password123"
}
```

**Response (200 OK):**
```json
{
    "message": "Login successful",
    "userId": "5",
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER"
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "password": "password123"
  }'
```

---

### 3. User Logout
**Endpoint:** `POST /auth/logout`

**Description:** Logout user

**Response (200 OK):**
```json
"Logged out successfully"
```

**Example:**
```bash
curl -X POST http://localhost:8080/auth/logout
```

---

## 🛍️ Product APIs

### 1. Get All Products (with Filters)
**Endpoint:** `GET /products`

**Description:** Get all products with optional filtering and pagination

**Query Parameters:**
- `search` (optional): Search by product name
- `categoryId` (optional): Filter by category ID
- `isActive` (optional): Filter by active status (true/false)
- `isFeatured` (optional): Filter by featured status (true/false)
- `page` (optional): Page number (default: 0)
- `size` (optional): Page size (default: 10)
- `sortBy` (optional): Sort field (default: createdAt)
- `direction` (optional): Sort direction (asc/desc, default: desc)
- `minPrice` (optional): Minimum price filter
- `maxPrice` (optional): Maximum price filter

**Response (200 OK):**
```json
{
    "content": [
        {
            "id": 1,
            "name": "Men's Casual Linen Shirt",
            "description": "Regular fit long sleeve textured linen shirt",
            "price": 499.0,
            "stock": 50,
            "isActive": true,
            "isFeatured": true,
            "createdAt": "2026-01-30T11:53:26.781334",
            "updatedAt": "2026-01-31T20:23:39.621423",
            "category": {
                "id": 1,
                "name": "Mens",
                "description": "Men's Clothing Section"
            },
            "images": [
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg"
            ]
        }
    ],
    "pageable": {
        "pageNumber": 0,
        "pageSize": 10
    },
    "totalElements": 22,
    "totalPages": 3
}
```

**Examples:**
```bash
# Get all products
curl -X GET http://localhost:8080/products

# Search products
curl -X GET "http://localhost:8080/products?search=shirt"

# Filter by category
curl -X GET "http://localhost:8080/products?categoryId=1"

# Filter by price range
curl -X GET "http://localhost:8080/products?minPrice=100&maxPrice=500"

# Pagination
curl -X GET "http://localhost:8080/products?page=0&size=5"
```

---

### 2. Get Product by ID
**Endpoint:** `GET /products/{id}`

**Description:** Get a single product by ID

**Path Parameters:**
- `id`: Product ID

**Response (200 OK):**
```json
{
    "id": 1,
    "name": "Men's Casual Linen Shirt",
    "description": "Regular fit long sleeve textured linen shirt",
    "price": 499.0,
    "stock": 50,
    "isActive": true,
    "isFeatured": true,
    "createdAt": "2026-01-30T11:53:26.781334",
    "updatedAt": "2026-01-31T20:23:39.621423",
    "category": {
        "id": 1,
        "name": "Mens",
        "description": "Men's Clothing Section"
    },
    "images": [
        "https://example.com/image1.jpg",
        "https://example.com/image2.jpg"
    ]
}
```

**Example:**
```bash
curl -X GET http://localhost:8080/products/1
```

---

### 3. Get Products by Category
**Endpoint:** `GET /products/category/{categoryId}`

**Description:** Get all products in a specific category

**Path Parameters:**
- `categoryId`: Category ID

**Response (200 OK):**
```json
[
    {
        "id": 1,
        "name": "Men's Casual Linen Shirt",
        "description": "Regular fit long sleeve textured linen shirt",
        "price": 499.0,
        "stock": 50,
        "isActive": true,
        "isFeatured": true,
        "category": {
            "id": 1,
            "name": "Mens",
            "description": "Men's Clothing Section"
        }
    }
]
```

**Example:**
```bash
curl -X GET http://localhost:8080/products/category/1
```

---

### 4. Create Product
**Endpoint:** `POST /products`

**Description:** Create a new product with images

**Content-Type:** `multipart/form-data`

**Request Parts:**
- `product` (JSON): Product data
- `images` (Files): Product images (multiple)

**Product JSON:**
```json
{
    "name": "New Product",
    "description": "Product description",
    "price": 299.99,
    "stock": 100,
    "categoryId": 1,
    "isActive": true,
    "isFeatured": false
}
```

**Response (201 Created):**
```json
{
    "id": 23,
    "name": "New Product",
    "description": "Product description",
    "price": 299.99,
    "stock": 100,
    "isActive": true,
    "isFeatured": false,
    "createdAt": "2026-03-03T00:45:00.000",
    "category": {
        "id": 1,
        "name": "Mens"
    }
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/products \
  -F "product={\"name\":\"New Product\",\"description\":\"Product description\",\"price\":299.99,\"stock\":100,\"categoryId\":1,\"isActive\":true,\"isFeatured\":false}" \
  -F "images=@image1.jpg" \
  -F "images=@image2.jpg"
```

---

### 5. Update Product
**Endpoint:** `PUT /products/{id}`

**Description:** Update an existing product

**Content-Type:** `multipart/form-data`

**Path Parameters:**
- `id`: Product ID

**Request Parts:**
- `product` (JSON): Updated product data
- `images` (Files, optional): New product images

**Response (200 OK):**
```json
{
    "id": 1,
    "name": "Updated Product Name",
    "description": "Updated description",
    "price": 599.99,
    "stock": 75,
    "isActive": true,
    "isFeatured": true
}
```

**Example:**
```bash
curl -X PUT http://localhost:8080/products/1 \
  -F "product={\"name\":\"Updated Product Name\",\"price\":599.99,\"stock\":75}"
```

---

### 6. Delete Product
**Endpoint:** `DELETE /products/{id}`

**Description:** Delete a product by ID

**Path Parameters:**
- `id`: Product ID

**Response (204 No Content):**
```
(no content)
```

**Example:**
```bash
curl -X DELETE http://localhost:8080/products/1
```

---

### 7. Delete Product Image
**Endpoint:** `DELETE /products/{productId}/images`

**Description:** Delete a specific image from a product

**Path Parameters:**
- `productId`: Product ID

**Query Parameters:**
- `imageUrl`: URL of image to delete

**Response (200 OK):**
```json
{
    "id": 1,
    "name": "Product Name",
    "images": ["remaining-image-urls"]
}
```

**Example:**
```bash
curl -X DELETE "http://localhost:8080/products/1/images?imageUrl=https://example.com/image.jpg"
```

---

### 8. Update Product Flags
**Endpoint:** `PATCH /products/{id}`

**Description:** Update specific product fields (isActive, isFeatured)

**Path Parameters:**
- `id`: Product ID

**Request Body:**
```json
{
    "isActive": false,
    "isFeatured": true
}
```

**Response (200 OK):**
```json
{
    "id": 1,
    "name": "Product Name",
    "isActive": false,
    "isFeatured": true
}
```

**Example:**
```bash
curl -X PATCH http://localhost:8080/products/1 \
  -H "Content-Type: application/json" \
  -d '{"isActive": false, "isFeatured": true}'
```

---

## 📂 Category APIs

### 1. Get All Categories
**Endpoint:** `GET /categories`

**Description:** Get all categories with product counts

**Response (200 OK):**
```json
[
    {
        "id": 1,
        "name": "Mens",
        "description": "Men's Clothing Section",
        "isActive": true,
        "createdAt": "2026-01-29T23:42:34.260592",
        "updatedAt": "2026-02-01T21:52:30.047142",
        "productCount": 8
    },
    {
        "id": 2,
        "name": "Womens",
        "description": "Women's Clothing Section",
        "isActive": true,
        "createdAt": "2026-01-29T23:43:05.810267",
        "updatedAt": "2026-02-02T01:10:19.346619",
        "productCount": 12
    }
]
```

**Example:**
```bash
curl -X GET http://localhost:8080/categories
```

---

### 2. Create Category
**Endpoint:** `POST /categories`

**Description:** Create a new category

**Request Body:**
```json
{
    "name": "Electronics",
    "description": "Electronic devices and accessories",
    "isActive": true
}
```

**Response (201 Created):**
```json
{
    "id": 8,
    "name": "Electronics",
    "description": "Electronic devices and accessories",
    "isActive": true,
    "createdAt": "2026-03-03T00:45:00.000",
    "updatedAt": "2026-03-03T00:45:00.000"
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Electronics",
    "description": "Electronic devices and accessories",
    "isActive": true
  }'
```

---

### 3. Update Category
**Endpoint:** `PUT /categories/{id}`

**Description:** Update an existing category

**Path Parameters:**
- `id`: Category ID

**Request Body:**
```json
{
    "name": "Updated Category Name",
    "description": "Updated description",
    "isActive": true
}
```

**Response (200 OK):**
```json
{
    "id": 1,
    "name": "Updated Category Name",
    "description": "Updated description",
    "isActive": true,
    "createdAt": "2026-01-29T23:42:34.260592",
    "updatedAt": "2026-03-03T00:45:00.000"
}
```

**Example:**
```bash
curl -X PUT http://localhost:8080/categories/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Category Name",
    "description": "Updated description"
  }'
```

---

### 4. Delete Category
**Endpoint:** `DELETE /categories/{id}`

**Description:** Delete a category by ID

**Path Parameters:**
- `id`: Category ID

**Response (204 No Content):**
```
(no content)
```

**Example:**
```bash
curl -X DELETE http://localhost:8080/categories/1
```

---

## 🧪 Test APIs

### 1. Secure Test Endpoint
**Endpoint:** `GET /test/secure`

**Description:** Test endpoint for JWT authentication (currently open)

**Response (200 OK):**
```json
"JWT working, access granted"
```

**Example:**
```bash
curl -X GET http://localhost:8080/test/secure
```

---

## 📸 Image Upload APIs

### 1. Test Image Upload
**Endpoint:** `POST /test/upload`

**Description:** Test image upload functionality

**Content-Type:** `multipart/form-data`

**Request Parameters:**
- `file`: Image file to upload

**Response (200 OK):**
```json
"https://res.cloudinary.com/your-cloud/image/upload/v1234567890/sample.jpg"
```

**Example:**
```bash
curl -X POST http://localhost:8080/test/upload \
  -F "file=@image.jpg"
```

---

## ❌ Error Responses

### Common Error Formats

**400 Bad Request:**
```json
{
    "timestamp": "2026-03-03T00:45:00.000",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed for object='product'. Error count: 1"
}
```

**401 Unauthorized:**
```json
{
    "timestamp": "2026-03-03T00:45:00.000",
    "status": 401,
    "error": "Unauthorized",
    "message": "Invalid Email or password"
}
```

**404 Not Found:**
```json
{
    "timestamp": "2026-03-03T00:45:00.000",
    "status": 404,
    "error": "Not Found",
    "message": "Product not found with id: 999"
}
```

**409 Conflict:**
```json
{
    "timestamp": "2026-03-03T00:45:00.000",
    "status": 409,
    "error": "Conflict",
    "message": "Email Already Registered"
}
```

**500 Internal Server Error:**
```json
{
    "timestamp": "2026-03-03T00:45:00.000",
    "status": 500,
    "error": "Internal Server Error",
    "message": "Database connection failed"
}
```

---

## 📊 Data Models

### User Model
```json
{
    "id": 1,
    "name": "John Doe",
    "email": "john@example.com",
    "role": "USER",
    "isActive": true,
    "createdAt": "2026-02-03T12:58:46.098121",
    "updatedAt": "2026-02-03T12:58:46.098121"
}
```

### Product Model
```json
{
    "id": 1,
    "name": "Product Name",
    "description": "Product description",
    "price": 499.99,
    "stock": 50,
    "isActive": true,
    "isFeatured": false,
    "createdAt": "2026-01-30T11:53:26.781334",
    "updatedAt": "2026-01-31T20:23:39.621423",
    "category": {
        "id": 1,
        "name": "Category Name",
        "description": "Category description"
    },
    "images": [
        "https://example.com/image1.jpg",
        "https://example.com/image2.jpg"
    ]
}
```

### Category Model
```json
{
    "id": 1,
    "name": "Category Name",
    "description": "Category description",
    "isActive": true,
    "createdAt": "2026-01-29T23:42:34.260592",
    "updatedAt": "2026-02-01T21:52:30.047142"
}
```

---

## 🔧 Quick Reference

### Authentication
- `POST /auth/register` - Register user
- `POST /auth/login` - Login user
- `POST /auth/logout` - Logout user

### Products (CRUD)
- `GET /products` - List products (with filters)
- `POST /products` - Create product (with images)
- `GET /products/{id}` - Get single product
- `PUT /products/{id}` - Update product
- `DELETE /products/{id}` - Delete product
- `PATCH /products/{id}` - Update product flags

### Categories (CRUD)
- `GET /categories` - List categories
- `POST /categories` - Create category
- `PUT /categories/{id}` - Update category
- `DELETE /categories/{id}` - Delete category

### Special
- `GET /products/category/{id}` - Products by category
- `DELETE /products/{id}/images` - Delete product image
- `POST /test/upload` - Test image upload
- `GET /test/secure` - Test secure endpoint

---

## 🚀 Usage Examples

### Complete E-commerce Flow
```bash
# 1. Register user
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"John Doe","email":"john@example.com","password":"password123"}'

# 2. Login user
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"john@example.com","password":"password123"}'

# 3. Get all categories
curl -X GET http://localhost:8080/categories

# 4. Get products in category
curl -X GET http://localhost:8080/products/category/1

# 5. Search products
curl -X GET "http://localhost:8080/products?search=shirt"

# 6. Get product details
curl -X GET http://localhost:8080/products/1

# 7. Logout
curl -X POST http://localhost:8080/auth/logout
```

---

## 📱 Frontend Integration

```javascript
// API Base URL
const API_BASE = 'http://localhost:8080';

// Authentication
const auth = {
  register: (userData) => 
    fetch(`${API_BASE}/auth/register`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(userData)
    }),
  
  login: (credentials) => 
    fetch(`${API_BASE}/auth/login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(credentials)
    }).then(res => res.json()),
  
  logout: () => 
    fetch(`${API_BASE}/auth/logout`, { method: 'POST' })
};

// Products
const products = {
  getAll: (params = {}) => {
    const query = new URLSearchParams(params).toString();
    return fetch(`${API_BASE}/products?${query}`).then(res => res.json());
  },
  
  getById: (id) => 
    fetch(`${API_BASE}/products/${id}`).then(res => res.json()),
  
  getByCategory: (categoryId) => 
    fetch(`${API_BASE}/products/category/${categoryId}`).then(res => res.json()),
  
  create: (formData) => 
    fetch(`${API_BASE}/products`, {
      method: 'POST',
      body: formData
    }).then(res => res.json()),
  
  update: (id, formData) => 
    fetch(`${API_BASE}/products/${id}`, {
      method: 'PUT',
      body: formData
    }).then(res => res.json()),
  
  delete: (id) => 
    fetch(`${API_BASE}/products/${id}`, { method: 'DELETE' })
};

// Categories
const categories = {
  getAll: () => 
    fetch(`${API_BASE}/categories`).then(res => res.json()),
  
  create: (categoryData) => 
    fetch(`${API_BASE}/categories`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(categoryData)
    }).then(res => res.json()),
  
  update: (id, categoryData) => 
    fetch(`${API_BASE}/categories/${id}`, {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(categoryData)
    }).then(res => res.json()),
  
  delete: (id) => 
    fetch(`${API_BASE}/categories/${id}`, { method: 'DELETE' })
};
```

---

## 🎯 Summary

This API provides a complete e-commerce backend with:
- ✅ **User Authentication** (Register, Login, Logout)
- ✅ **Product Management** (CRUD with images)
- ✅ **Category Management** (CRUD)
- ✅ **Advanced Filtering** (Search, pagination, price filters)
- ✅ **Image Upload** (Cloudinary integration)
- ✅ **Simple Authentication** (No JWT tokens required)
- ✅ **Open Access** (All endpoints publicly accessible)

Perfect for e-commerce applications! 🛍️
