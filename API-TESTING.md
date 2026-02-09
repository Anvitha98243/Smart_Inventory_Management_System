# API Testing Guide
## Smart Inventory Management - Authentication Module

Test all endpoints using curl, Postman, or any HTTP client.

---

## Base URL
```
http://localhost:8080/api/auth
```

---

## 1. Test Connection

### cURL:
```bash
curl http://localhost:8080/api/auth/test
```

### Expected Response:
```
Authentication API is working!
```

---

## 2. User Registration (Signup)

### cURL:
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "test123",
    "fullName": "Test User",
    "role": "EMPLOYEE"
  }'
```

### Postman:
- Method: POST
- URL: http://localhost:8080/api/auth/signup
- Headers: Content-Type: application/json
- Body (raw JSON):
```json
{
  "username": "testuser",
  "email": "test@example.com",
  "password": "test123",
  "fullName": "Test User",
  "role": "EMPLOYEE"
}
```

### Expected Response:
```json
{
  "success": true,
  "message": "User registered successfully!",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 3,
    "username": "testuser",
    "email": "test@example.com",
    "fullName": "Test User",
    "role": "EMPLOYEE",
    "isActive": true,
    "createdAt": "2026-02-08T10:30:00"
  }
}
```

---

## 3. User Login

### cURL:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "admin123"
  }'
```

### Postman:
- Method: POST
- URL: http://localhost:8080/api/auth/login
- Headers: Content-Type: application/json
- Body (raw JSON):
```json
{
  "username": "admin",
  "password": "admin123"
}
```

### Expected Response:
```json
{
  "success": true,
  "message": "Login successful!",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "username": "admin",
    "email": "admin@inventory.com",
    "fullName": "System Administrator",
    "role": "ADMIN",
    "isActive": true,
    "createdAt": "2026-02-08T10:00:00",
    "lastLogin": "2026-02-08T12:30:00"
  }
}
```

**Save the token for subsequent requests!**

---

## 4. Forgot Password (Request Reset Token)

### cURL:
```bash
curl -X POST http://localhost:8080/api/auth/forgot-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@inventory.com"
  }'
```

### Postman:
- Method: POST
- URL: http://localhost:8080/api/auth/forgot-password
- Headers: Content-Type: application/json
- Body (raw JSON):
```json
{
  "email": "admin@inventory.com"
}
```

### Expected Response:
```json
{
  "success": true,
  "message": "Reset token generated. Token: a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6"
}
```

**Copy the reset token!**

---

## 5. Reset Password

### cURL:
```bash
curl -X POST http://localhost:8080/api/auth/reset-password \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@inventory.com",
    "resetToken": "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6",
    "newPassword": "newpassword123"
  }'
```

### Postman:
- Method: POST
- URL: http://localhost:8080/api/auth/reset-password
- Headers: Content-Type: application/json
- Body (raw JSON):
```json
{
  "email": "admin@inventory.com",
  "resetToken": "a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5p6",
  "newPassword": "newpassword123"
}
```

### Expected Response:
```json
{
  "success": true,
  "message": "Password reset successful!"
}
```

---

## 6. Get All Users (Admin Only)

### cURL:
```bash
curl -X GET http://localhost:8080/api/auth/users \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### Postman:
- Method: GET
- URL: http://localhost:8080/api/auth/users
- Headers: 
  - Authorization: Bearer YOUR_JWT_TOKEN_HERE

### Expected Response:
```json
[
  {
    "id": 1,
    "username": "admin",
    "email": "admin@inventory.com",
    "fullName": "System Administrator",
    "role": "ADMIN",
    "isActive": true,
    "createdAt": "2026-02-08T10:00:00"
  },
  {
    "id": 2,
    "username": "john_doe",
    "email": "john@inventory.com",
    "fullName": "John Doe",
    "role": "EMPLOYEE",
    "isActive": true,
    "createdAt": "2026-02-08T10:00:00"
  }
]
```

---

## 7. Get User by ID

### cURL:
```bash
curl -X GET http://localhost:8080/api/auth/users/1 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### Postman:
- Method: GET
- URL: http://localhost:8080/api/auth/users/1
- Headers: 
  - Authorization: Bearer YOUR_JWT_TOKEN_HERE

### Expected Response:
```json
{
  "id": 1,
  "username": "admin",
  "email": "admin@inventory.com",
  "fullName": "System Administrator",
  "role": "ADMIN",
  "isActive": true,
  "createdAt": "2026-02-08T10:00:00"
}
```

---

## 8. Delete User (Admin Only)

### cURL:
```bash
curl -X DELETE http://localhost:8080/api/auth/users/3 \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### Postman:
- Method: DELETE
- URL: http://localhost:8080/api/auth/users/3
- Headers: 
  - Authorization: Bearer YOUR_JWT_TOKEN_HERE

### Expected Response:
```json
{
  "success": true,
  "message": "User deleted successfully!"
}
```

---

## 9. Deactivate User (Admin Only)

### cURL:
```bash
curl -X PUT http://localhost:8080/api/auth/users/2/deactivate \
  -H "Authorization: Bearer YOUR_JWT_TOKEN_HERE"
```

### Postman:
- Method: PUT
- URL: http://localhost:8080/api/auth/users/2/deactivate
- Headers: 
  - Authorization: Bearer YOUR_JWT_TOKEN_HERE

### Expected Response:
```json
{
  "success": true,
  "message": "User deactivated successfully!"
}
```

---

## Error Responses

### Invalid Credentials:
```json
{
  "success": false,
  "message": "Invalid username or password!"
}
```

### User Already Exists:
```json
{
  "success": false,
  "message": "Username already exists!"
}
```

### Invalid Reset Token:
```json
{
  "success": false,
  "message": "Invalid reset token!"
}
```

### Expired Reset Token:
```json
{
  "success": false,
  "message": "Reset token has expired!"
}
```

---

## Postman Collection

You can import this into Postman as a collection:

```json
{
  "info": {
    "name": "Smart Inventory Auth API",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "item": [
    {
      "name": "Signup",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"username\": \"testuser\",\n  \"email\": \"test@example.com\",\n  \"password\": \"test123\",\n  \"fullName\": \"Test User\",\n  \"role\": \"EMPLOYEE\"\n}"
        },
        "url": {"raw": "http://localhost:8080/api/auth/signup"}
      }
    },
    {
      "name": "Login",
      "request": {
        "method": "POST",
        "header": [{"key": "Content-Type", "value": "application/json"}],
        "body": {
          "mode": "raw",
          "raw": "{\n  \"username\": \"admin\",\n  \"password\": \"admin123\"\n}"
        },
        "url": {"raw": "http://localhost:8080/api/auth/login"}
      }
    }
  ]
}
```

---

## Testing Workflow

1. **Test Connection** - Verify API is running
2. **Login** - Get JWT token
3. **Save Token** - Use in subsequent requests
4. **Test Protected Endpoints** - Users list, delete, etc.
5. **Test Password Reset Flow** - Request token → Reset password
6. **Test Validation** - Try invalid data, missing fields

---

**Happy Testing! 🚀**
