# User Authentication API Documentation

## Base URL
```
http://localhost:8080
```

## Authentication Endpoints

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

### 2. User Login
**Endpoint:** `POST /auth/login`

**Description:** Authenticate user and return JWT token

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
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwidXNlcklkIjoxLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc3MjQ3ODI5NCwiZXhwIjoxNzcyNDc5MTk0fQ.example-token",
    "tokenType": "Bearer"
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

## Using JWT Token for Protected Endpoints

Once you get the JWT token from login, include it in the Authorization header for protected API calls:

**Authorization Header:**
```
Authorization: Bearer <your-jwt-token>
```

**Example:**
```bash
curl -X GET http://localhost:8080/products \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGV4YW1wbGUuY29tIiwidXNlcklkIjoxLCJyb2xlIjoiVVNFUiIsImlhdCI6MTc3MjQ3ODI5NCwiZXhwIjoxNzcyNDc5MTk0fQ.example-token"
```

---

## Complete API Examples

### Register a New User
```bash
# Step 1: Register
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Alice Smith",
    "email": "alice@example.com",
    "password": "alicepass123"
  }'

# Response: "User Registered Successfully"
```

### Login and Get Token
```bash
# Step 2: Login
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "alice@example.com",
    "password": "alicepass123"
  }'

# Response: 
# {
#   "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGljZUBleGFtcGxlLmNvbSIsInVzZXJJZCI6Mywicm9sZSI6IlVTRVIiLCJpYXQiOjE3NzI0NzgyOTQsImV4cCI6MTc3MjQ3OTE5NH0.example-token",
#   "tokenType": "Bearer"
# }
```

### Use Token for Protected API
```bash
# Step 3: Access protected endpoint with token
curl -X GET http://localhost:8080/products \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhbGljZUBleGFtcGxlLmNvbSIsInVzZXJJZCI6Mywicm9sZSI6IlVTRVIiLCJpYXQiOjE3NzI0NzgyOTQsImV4cCI6MTc3MjQ3OTE5NH0.example-token"
```

---

## Error Responses

### Registration Errors
- **400 Bad Request:** Invalid input data
- **409 Conflict:** Email already exists

### Login Errors
- **401 Unauthorized:** Invalid email or password
- **400 Bad Request:** Missing required fields

### Token Errors
- **401 Unauthorized:** Invalid or expired token
- **403 Forbidden:** Insufficient permissions

---

## Testing with Postman

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

### 3. Access Protected APIs
- **Method:** GET/POST/PUT/DELETE
- **URL:** Any protected endpoint
- **Headers:** `Authorization: Bearer <token>`

---

## User Roles

The system supports two user roles:
- **USER:** Regular customer access
- **ADMIN:** Administrative access

New registrations default to **USER** role.

---

## Token Expiration

JWT tokens expire after **1 hour**. After expiration, users need to login again to get a new token.

---

## Logout

Since JWT tokens are stateless, there's no explicit logout endpoint. To logout:
1. Client-side: Delete the stored token
2. Server-side: Token will automatically expire after 1 hour
