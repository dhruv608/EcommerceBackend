# Simple User Authentication API Documentation

## Base URL
```
http://localhost:8080
```

## 🔐 Simple Authentication Endpoints (No Tokens Required)

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

**Example Request:**
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

### 2. User Login (Simple - No JWT)
**Endpoint:** `POST /auth/login`

**Description:** Authenticate user and return user info (no tokens)

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

**Example Request:**
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

**Description:** Logout user (simple response)

**Response (200 OK):**
```json
"Logged out successfully"
```

**Example Request:**
```bash
curl -X POST http://localhost:8080/auth/logout
```

---

## 🛍️ Product APIs (No Authentication Required)

### Get All Products
**Endpoint:** `GET /products`

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
            "category": {
                "id": 1,
                "name": "Mens",
                "description": "Men's Clothing Section"
            }
        }
    ]
}
```

**Example Request:**
```bash
curl -X GET http://localhost:8080/products
```

---

## 🎯 Complete Working Example

### Step 1: Register a New User
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Smith",
    "email": "alice@example.com",
    "password": "alicepass123"
  }'

# Response: "User Registered Successfully"
```

### Step 2: Login (No Token Needed)
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alice@example.com",
    "password": "alicepass123"
  }'

# Response: 
# {
#   "message": "Login successful",
#   "userId": "6",
#   "name": "Alice Smith",
#   "email": "alice@example.com",
#   "role": "USER"
# }
```

### Step 3: Access Products (No Auth Required)
```bash
curl -X GET http://localhost:8080/products

# Returns all products with full details
```

### Step 4: Logout
```bash
curl -X POST http://localhost:8080/auth/logout

# Response: "Logged out successfully"
```

---

## 📋 All Available Endpoints

### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - Login user (returns user info)
- `POST /auth/logout` - Logout user

### Products
- `GET /products` - Get all products
- `GET /products/{id}` - Get single product
- `GET /products/category/{categoryId}` - Get products by category
- `GET /products?search=keyword` - Search products

### Categories
- `GET /categories` - Get all categories

---

## 🚀 Key Features

✅ **No JWT Tokens** - Simple authentication  
✅ **No Headers Required** - Just login and use  
✅ **Session-Based** - Client manages login state  
✅ **All APIs Open** - No authentication required for products  
✅ **Simple Response** - Clean user info returned  
✅ **Easy Integration** - Perfect for frontend development  

---

## 🔒 Security Notes

- This is a **development-friendly** setup
- All endpoints are publicly accessible
- In production, you'd want to add proper authentication
- Passwords are still encrypted with BCrypt
- User data is validated and stored securely

---

## 🧪 Testing with Postman

### 1. Register User
- **Method:** POST
- **URL:** `http://localhost:8080/auth/register`
- **Headers:** `Content-Type: application/json`
- **Body:** Raw JSON with name, email, password

### 2. Login User
- **Method:** POST
- **URL:** `http://localhost:8080/auth/login`
- **Headers:** `Content-Type: application/json`
- **Body:** Raw JSON with email, password

### 3. Get Products
- **Method:** GET
- **URL:** `http://localhost:8080/products`
- **No headers required**

---

## 📱 Frontend Integration

```javascript
// Register User
const register = async (name, email, password) => {
  const response = await fetch('http://localhost:8080/auth/register', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ name, email, password })
  });
  return response.text();
};

// Login User
const login = async (email, password) => {
  const response = await fetch('http://localhost:8080/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ email, password })
  });
  return response.json(); // Returns user info
};

// Get Products
const getProducts = async () => {
  const response = await fetch('http://localhost:8080/products');
  return response.json();
};
```

Perfect for simple applications! 🎉
